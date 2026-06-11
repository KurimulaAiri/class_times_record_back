@echo off
REM ====================================================================
REM Course Record Platform - One-Click Deploy Script (Windows .bat)
REM Usage:  double-click or run: deploy.bat
REM Flow:   Check prerequisites -> Maven build -> Docker compose up -d --build
REM ====================================================================
setlocal enabledelayedexpansion

set "PROJECT_ROOT=%~dp0"
set "COMPOSE_FILE=%PROJECT_ROOT%docker-compose.yml"

echo.
echo ===============================================================
echo   Course Record Platform - One-Click Deploy (Windows)
echo ===============================================================
echo   Project root: %PROJECT_ROOT%
echo   Services:     gateway, auth-service, business-service, nacos, nginx, sentinel
echo ===============================================================
echo.

REM =====================================================================
REM Step 1: Check prerequisites
REM =====================================================================
echo [1/6] Checking prerequisites...

where mvn >nul 2>&1
if errorlevel 1 (
    echo   [FAIL] Maven not found in PATH. Install Maven or run set-jdk.ps1 first.
    pause
    exit /b 1
)
echo   [OK]  Maven found.

where docker >nul 2>&1
if errorlevel 1 (
    echo   [FAIL] Docker not found in PATH. Install Docker Desktop.
    pause
    exit /b 1
)
echo   [OK]  Docker found.

docker compose version >nul 2>&1
if errorlevel 1 (
    echo   [FAIL] 'docker compose' not available. Install Docker Compose v2.
    pause
    exit /b 1
)
echo   [OK]  Docker Compose found.

if not exist "%COMPOSE_FILE%" (
    echo   [FAIL] docker-compose.yml not found at: %COMPOSE_FILE%
    pause
    exit /b 1
)
echo   [OK]  Compose file: docker-compose.yml

REM =====================================================================
REM Step 2: Stop existing containers
REM =====================================================================
echo.
echo [2/6] Stopping existing containers...
cd /d "%PROJECT_ROOT%"
docker compose down >nul 2>&1
if errorlevel 1 (
    echo   [INFO] No running containers to stop.
) else (
    echo   [OK]  Existing containers stopped.
)

REM =====================================================================
REM Step 3: Maven clean + package
REM =====================================================================
echo.
echo [3/6] Building JARs with Maven (skip tests)...
cd /d "%PROJECT_ROOT%"
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo   [FAIL] Maven build failed. Check errors above.
    pause
    exit /b 1
)
echo   [OK]  Maven build completed.

for %%s in (gateway auth-service business-service) do (
    set "jarfile="
    for /f "delims=" %%f in ('dir /b "%PROJECT_ROOT%%%s\target\%%s-*.jar" 2^>nul ^| findstr /v /i "sources javadoc original"') do (
        set "jarfile=%%f"
    )
    if defined jarfile (
        echo     %%s -^> !jarfile!
    ) else (
        echo     %%s -^> JAR NOT FOUND in target\
    )
)

REM =====================================================================
REM Step 4: Clean old images
REM =====================================================================
echo.
echo [4/6] Cleaning old course-record images...
for /f "delims=" %%i in ('docker images --format "{{.Repository}}:{{.Tag}}" 2^>nul ^| findstr /r "^cr-gateway ^cr-auth-service ^cr-business-service"') do (
    docker rmi -f %%i >nul 2>&1
)
echo   [OK]  Old images cleaned.

REM =====================================================================
REM Step 5: Docker compose build + up
REM =====================================================================
echo.
echo [5/6] Building Docker images & starting services...
cd /d "%PROJECT_ROOT%"
docker compose build --no-cache
if errorlevel 1 (
    echo   [FAIL] Docker compose build failed.
    pause
    exit /b 1
)
echo   [OK]  All images built.

docker compose up -d
if errorlevel 1 (
    echo   [FAIL] Docker compose up failed.
    pause
    exit /b 1
)
echo   [OK]  Services started.

REM =====================================================================
REM Step 6: Status
REM =====================================================================
echo.
echo [6/6] Deployment status...
echo.
docker compose ps
echo.

echo ===============================================================
echo   Deploy finished.
echo   Gateway:  http://localhost:9080
echo   Nacos:    http://localhost:8848/nacos (nacos/nacos)
echo   Sentinel: http://localhost:8080 (sentinel/sentinel)
echo.
echo   Wait 60-120 seconds for Nacos + services to fully initialize.
echo   View logs: docker compose logs -f [service-name]
echo   Stop all:  docker compose down
echo ===============================================================
echo.
endlocal
pause
