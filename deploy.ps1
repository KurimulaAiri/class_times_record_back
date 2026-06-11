# ====================================================================
# Course Record Platform - One-Click Deploy Script (Windows)
# Usage:  .\deploy.ps1
# Flow:   Check prerequisites -> Maven build -> Docker compose up -d --build
# ====================================================================

$ErrorActionPreference = "Stop"

# --- Config -----------------------------------------------------------
$PROJECT_ROOT = Split-Path -Parent $MyInvocation.MyCommand.Path
$COMPOSE_FILE = Join-Path $PROJECT_ROOT "docker-compose.yml"
$SERVICES = @("gateway", "auth-service", "business-service")

# --- Color helpers ----------------------------------------------------
function Write-Step { param($msg)  Write-Host ""; Write-Host "==> $msg" -ForegroundColor Cyan }
function Write-Ok   { param($msg)  Write-Host "    [OK]  $msg" -ForegroundColor Green }
function Write-Warn { param($msg)  Write-Host "    [WARN] $msg" -ForegroundColor Yellow }
function Write-Fail { param($msg)  Write-Host "    [FAIL] $msg" -ForegroundColor Red; exit 1 }

# --- Banner -----------------------------------------------------------
Write-Host ""
Write-Host "===============================================================" -ForegroundColor Magenta
Write-Host "  Course Record Platform - One-Click Deploy" -ForegroundColor Magenta
Write-Host "===============================================================" -ForegroundColor Magenta
Write-Host "  Project root: $PROJECT_ROOT"
Write-Host "  Services:     $($SERVICES -join ', '), nacos, nginx, sentinel"
Write-Host "==============================================================="

# =====================================================================
# Step 1: Check prerequisites
# =====================================================================
Write-Step "Checking prerequisites"

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Fail "Maven not found in PATH. Please install Maven or run: set-jdk.ps1"
}
Write-Ok "Maven: $((mvn -v 2>$null)[0])"

if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Fail "Docker not found in PATH. Please install Docker Desktop."
}
Write-Ok "Docker: $((docker --version) -join ' ')"

$composeOk = $false
try {
    docker compose version | Out-Null; $composeOk = $true
} catch { $composeOk = $false }
if (-not $composeOk) {
    Write-Fail "'docker compose' not available. Please install Docker Compose v2."
}
Write-Ok "Docker Compose: $((docker compose version) -join ' ')"

if (-not (Test-Path $COMPOSE_FILE)) {
    Write-Fail "docker-compose.yml not found at: $COMPOSE_FILE"
}
Write-Ok "Compose file: $COMPOSE_FILE"

# =====================================================================
# Step 2: Stop existing containers
# =====================================================================
Write-Step "Stopping existing containers"
Push-Location $PROJECT_ROOT
try {
    $running = (docker compose ps --format '{{.Names}}' 2>$null) | Where-Object { $_ -match '^cr-' }
    if ($running -and $running.Count -gt 0) {
        docker compose down
        Write-Ok "Stopped $($running.Count) containers"
    } else {
        Write-Ok "No running Course Record containers found"
    }
} finally {
    Pop-Location
}

# =====================================================================
# Step 3: Maven clean + package
# =====================================================================
Write-Step "Building JARs with Maven (skip tests)"
Push-Location $PROJECT_ROOT
try {
    mvn clean package -DskipTests -q
    if ($LASTEXITCODE -ne 0) {
        Write-Fail "Maven build failed (exit code $LASTEXITCODE). Fix errors above and retry). Deploy halted."
    }
    Write-Ok "Maven build completed"

    foreach ($svc in $SERVICES) {
        $jar = Get-ChildItem -Path (Join-Path $PROJECT_ROOT $svc\target) -Filter "$svc-*.jar" -File |
               Where-Object { $_.Name -notmatch 'sources|javadoc|original' } |
               Select-Object -First 1
        if ($jar) {
            $sizeKB = [math]::Round($jar.Length / 1KB, 0)
            Write-Ok ("  {0,-18} -> {1}  ({2} KB)" -f $svc, $jar.Name, $sizeKB)
        } else {
            Write-Warn "  $svc -> JAR NOT FOUND in target/"
        }
    }
} finally {
    Pop-Location
}

# =====================================================================
# Step 4: Clean old images (optional but recommended)
# =====================================================================
Write-Step "Cleaning old course-record images"
$oldImages = docker images --format '{{.Repository}}:{{.Tag}}' | Where-Object { $_ -match '^cr-(gateway|auth-service|business-service):' }
if ($oldImages) {
    foreach ($img in $oldImages) {
        docker rmi -f $img 2>&1 | Out-Null
    }
    Write-Ok "Removed $(@($oldImages).Count) old image(s)"
} else {
    Write-Ok "No old images to clean"
}

# =====================================================================
# Step 5: Docker compose build + up
# =====================================================================
Write-Step "Building Docker images & starting services"
Push-Location $PROJECT_ROOT
try {
    docker compose build --no-cache
    if ($LASTEXITCODE -ne 0) {
        Write-Fail "Docker compose build failed (exit code $LASTEXITCODE). Deploy halted."
    }
    Write-Ok "All images built successfully"

    docker compose up -d
    if ($LASTEXITCODE -ne 0) {
        Write-Fail "Docker compose up failed (exit code $LASTEXITCODE). Deploy halted."
    }
    Write-Ok "Services started"
} finally {
    Pop-Location
}

# =====================================================================
# Step 6: Status
# =====================================================================
Write-Step "Deployment status"
Push-Location $PROJECT_ROOT
try {
    Write-Host ""
    docker compose ps
    Write-Host ""
    Write-Host "  Wait 60-120 seconds for Nacos + services to fully initialize,"
    Write-Host "  then run:  docker compose logs -f [service-name]"
    Write-Host "  To stop:    docker compose down"
    Write-Host ""
} finally {
    Pop-Location
}

Write-Host ""
Write-Host "===============================================================" -ForegroundColor Green
Write-Host "  Deploy finished. Access gateway via: http://localhost:9080" -ForegroundColor Green
Write-Host "  Nacos: http://localhost:8848/nacos (user: nacos / pwd: nacos)"
Write-Host "  Sentinel: http://localhost:8080 (user: sentinel / pwd: sentinel)"
Write-Host "===============================================================" -ForegroundColor Green
