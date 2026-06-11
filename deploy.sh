#!/usr/bin/env bash
# ====================================================================
# Course Record Platform - One-Click Deploy Script (Linux / macOS)
# Usage:  chmod +x deploy.sh && ./deploy.sh
# Flow:   Check prerequisites -> Maven build -> Docker compose up -d --build
# ====================================================================

set -euo pipefail

# --- Config -----------------------------------------------------------
PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
COMPOSE_FILE="$PROJECT_ROOT/docker-compose.yml"
SERVICES=("gateway" "auth-service" "business-service")

# --- Color helpers ----------------------------------------------------
C_CYAN='\033[1;36m'
C_GREEN='\033[1;32m'
C_YELLOW='\033[1;33m'
C_RED='\033[1;31m'
C_MAGENTA='\033[1;35m'
C_NC='\033[0m'

step() { printf "\n${C_CYAN}==> %s${C_NC}\n" "$*"; }
ok()   { printf "    ${C_GREEN}[OK]${C_NC}  %s\n" "$*"; }
warn() { printf "    ${C_YELLOW}[WARN]${C_NC} %s\n" "$*"; }
fail() { printf "    ${C_RED}[FAIL]${C_NC} %s\n" "$*"; exit 1; }

# --- Banner -----------------------------------------------------------
printf "\n"
printf "${C_MAGENTA}===============================================================${C_NC}\n"
printf "${C_MAGENTA}  Course Record Platform - One-Click Deploy${C_NC}\n"
printf "${C_MAGENTA}===============================================================${C_NC}\n"
printf "  Project root: %s\n" "$PROJECT_ROOT"
printf "  Services:     %s, nacos, nginx, sentinel\n" "$(IFS=, ; echo "${SERVICES[*]}")"
printf "${C_MAGENTA}===============================================================${C_NC}\n"

# =====================================================================
# Step 1: Check prerequisites
# =====================================================================
step "Checking prerequisites"

if ! command -v mvn &>/dev/null; then
    fail "Maven not found in PATH. Please install Maven (requires JDK 21)."
fi
ok "Maven: $(mvn -v 2>/dev/null | head -n1)"

if ! command -v docker &>/dev/null; then
    fail "Docker not found in PATH. Please install Docker Engine."
fi
ok "Docker: $(docker --version)"

if ! docker compose version &>/dev/null; then
    fail "'docker compose' not available. Please install Docker Compose v2."
fi
ok "Docker Compose: $(docker compose version)"

if [ ! -f "$COMPOSE_FILE" ]; then
    fail "docker-compose.yml not found at: $COMPOSE_FILE"
fi
ok "Compose file: $COMPOSE_FILE"

# =====================================================================
# Step 2: Stop existing containers
# =====================================================================
step "Stopping existing containers"
cd "$PROJECT_ROOT"

running=$(docker compose ps --format '{{.Names}}' 2>/dev/null | grep -E '^cr-' || true)
if [ -n "$running" ]; then
    count=$(echo "$running" | wc -l | tr -d ' ')
    docker compose down
    ok "Stopped $count containers"
else
    ok "No running Course Record containers found"
fi

# =====================================================================
# Step 3: Maven clean + package
# =====================================================================
step "Building JARs with Maven (skip tests)"
cd "$PROJECT_ROOT"

if ! mvn clean package -DskipTests -q; then
    fail "Maven build failed. Fix errors above and retry. Deploy halted."
fi
ok "Maven build completed"

for svc in "${SERVICES[@]}"; do
    jar=$(find "$PROJECT_ROOT/$svc/target" -maxdepth 1 -type f -name "$svc-*.jar" \
        ! -name '*sources*' ! -name '*javadoc*' ! -name '*original*' 2>/dev/null | head -n1)
    if [ -n "$jar" ] && [ -f "$jar" ]; then
        size_kb=$(du -k "$jar" | cut -f1)
        ok "  $(printf '%-18s' "$svc") -> $(basename "$jar")  (${size_kb} KB)"
    else
        warn "  $svc -> JAR NOT FOUND in target/"
    fi
done

# =====================================================================
# Step 4: Clean old images
# =====================================================================
step "Cleaning old course-record images"

old_images=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep -E '^cr-(gateway|auth-service|business-service):' || true)
if [ -n "$old_images" ]; then
    count=$(echo "$old_images" | wc -l | tr -d ' ')
    echo "$old_images" | xargs -n1 docker rmi -f &>/dev/null || true
    ok "Removed $count old image(s)"
else
    ok "No old images to clean"
fi

# =====================================================================
# Step 5: Docker compose build + up
# =====================================================================
step "Building Docker images & starting services"
cd "$PROJECT_ROOT"

if ! docker compose build --no-cache; then
    fail "Docker compose build failed. Deploy halted."
fi
ok "All images built successfully"

if ! docker compose up -d; then
    fail "Docker compose up failed. Deploy halted."
fi
ok "Services started"

# =====================================================================
# Step 6: Status
# =====================================================================
step "Deployment status"
cd "$PROJECT_ROOT"

printf "\n"
docker compose ps
printf "\n"
printf "  Wait 60-120 seconds for Nacos + services to fully initialize,\n"
printf "  then run:  docker compose logs -f [service-name]\n"
printf "  To stop:    docker compose down\n"
printf "\n"

printf "\n"
printf "${C_GREEN}===============================================================${C_NC}\n"
printf "${C_GREEN}  Deploy finished. Access gateway via: http://localhost:9080${C_NC}\n"
printf "  Nacos:    http://localhost:8848/nacos (user: nacos / pwd: nacos)\n"
printf "  Sentinel: http://localhost:8080 (user: sentinel / pwd: sentinel)\n"
printf "${C_GREEN}===============================================================${C_NC}\n"
