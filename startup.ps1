param(
    [int]$BackendPort = 8080,
    [int]$FrontendPort = 3000,
    [switch]$SkipDatabase = $false
)

$ErrorActionPreference = "Stop"
$repoRoot = $PSScriptRoot
$backendDir = Join-Path $repoRoot "backend"
$frontendDir = Join-Path $repoRoot "frontend"
$backendLog = Join-Path $backendDir "startup-backend.log"
$frontendLog = Join-Path $frontendDir "startup-frontend.log"
$mavenLocalRepo = Join-Path $repoRoot ".m2\\repository"

function Require-Dependency {
    param([string]$Command)
    if (-not (Get-Command $Command -ErrorAction SilentlyContinue)) {
        throw "Missing dependency: $Command. Install it and re-run."
    }
}

function Wait-ForPort {
    param(
        [int]$Port,
        [int]$TimeoutSeconds = 45,
        [string]$Service
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)

    do {
        $listening = Get-NetTCPConnection -State Listen -LocalPort $Port -ErrorAction SilentlyContinue
        if ($listening) {
            return
        }

        Start-Sleep -Milliseconds 500
    } while ((Get-Date) -lt $deadline)

    throw "$Service did not start on port $Port within $TimeoutSeconds seconds."
}

function Start-BackgroundProcess {
    param(
        [string]$Name,
        [string]$Command,
        [string]$WorkingDirectory,
        [string]$StdOutLog,
        [string]$StdErrLog
    )

    Write-Host "Starting $Name..." -ForegroundColor Yellow
    $process = Start-Process -FilePath "cmd.exe" `
        -ArgumentList "/c", $Command `
        -WorkingDirectory $WorkingDirectory `
        -RedirectStandardOutput $StdOutLog `
        -RedirectStandardError $StdErrLog `
        -PassThru

    Write-Host "$Name PID: $($process.Id)" -ForegroundColor DarkGray
    return $process
}

try {
    Require-Dependency "docker"
    Require-Dependency "java"
    Require-Dependency "npm"
    Require-Dependency "node"
    if (-not (Test-Path (Join-Path $backendDir "mvnw.cmd"))) {
        throw "Missing backend/.mvnw wrapper. Run from the airline-api repository."
    }

    if (-not $SkipDatabase) {
        Write-Host "Starting PostgreSQL container..." -ForegroundColor Cyan
        Set-Location $backendDir
        $composeOutput = docker compose up -d 2>&1
        if ($LASTEXITCODE -ne 0) {
            Write-Host $composeOutput
            throw "Docker Compose failed. Install/start Docker Desktop and retry, or run with -SkipDatabase if DB is already running."
        }
        Set-Location $repoRoot
    }
    else {
        Write-Host "Skipping database start (--SkipDatabase)." -ForegroundColor Cyan
    }

    Write-Host "Starting backend..." -ForegroundColor Cyan
    $backendProcess = Start-BackgroundProcess -Name "Backend (Spring Boot)" `
        -Command ".\mvnw.cmd -Dmaven.repo.local=$mavenLocalRepo spring-boot:run -Dserver.port=$BackendPort" `
        -WorkingDirectory $backendDir `
        -StdOutLog $backendLog `
        -StdErrLog "$backendLog.err"

    if ($backendProcess.HasExited) {
        throw "Backend process exited immediately. Check $backendLog and $backendLog.err."
    }

    Write-Host "Checking frontend dependencies..." -ForegroundColor Cyan
    $frontendVite = Join-Path $frontendDir "node_modules\\.bin\\vite"
    if (-not (Test-Path $frontendVite)) {
        Write-Host "Installing frontend dependencies (first run only)..." -ForegroundColor Cyan
        $env:NPM_CONFIG_CACHE = Join-Path $frontendDir ".npm-cache"
        npm --prefix $frontendDir install
        if ($LASTEXITCODE -ne 0) {
            throw "Frontend dependency install failed. Check npm permissions and try again."
        }
        if (-not (Test-Path $frontendVite)) {
            throw "Frontend install succeeded but Vite binary is still missing."
        }
    }

    Write-Host "Starting frontend..." -ForegroundColor Cyan
    $frontendProcess = Start-BackgroundProcess -Name "Frontend (Vite)" `
        -Command "npm run dev -- --host 0.0.0.0 --port $FrontendPort" `
        -WorkingDirectory $frontendDir `
        -StdOutLog $frontendLog `
        -StdErrLog "$frontendLog.err"

    if ($frontendProcess.HasExited) {
        throw "Frontend process exited immediately. Check $frontendLog and $frontendLog.err."
    }

    Wait-ForPort -Port $BackendPort -Service "Backend"
    Write-Host "Backend is listening on $BackendPort." -ForegroundColor Green
    Wait-ForPort -Port $FrontendPort -Service "Frontend"
    Write-Host "Frontend is listening on $FrontendPort." -ForegroundColor Green

    Write-Host ""
    Write-Host "Airline stack started." -ForegroundColor Green
    Write-Host "Backend:  http://localhost:$BackendPort"
    Write-Host "Frontend: http://localhost:$FrontendPort"
    Write-Host ""
    Write-Host "Logs:"
    Write-Host "  Backend  - $backendLog and $backendLog.err"
    Write-Host "  Frontend - $frontendLog and $frontendLog.err"
    Write-Host ""
    Write-Host "To stop:"
    Write-Host "  Stop-Process -Id $($backendProcess.Id), $($frontendProcess.Id)"
    Write-Host ""
    Write-Host "Tip:"
    Write-Host "  If Docker is already running, use: .\startup.ps1 -SkipDatabase"
    Write-Host ""
}
catch {
    Write-Host "Startup failed: $($_.Exception.Message)" -ForegroundColor Red
    if ($backendProcess) {
        Write-Host "Backend log tail:"
        if (Test-Path $backendLog) {
            Get-Content -Path $backendLog -Tail 20
        }
    }
    if ($frontendProcess) {
        Write-Host "Frontend log tail:"
        if (Test-Path $frontendLog) {
            Get-Content -Path $frontendLog -Tail 20
        }
    }
    exit 1
}
