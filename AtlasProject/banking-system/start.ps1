# Indian Banking System - PowerShell Startup Script
# This script starts the banking system with proper configuration

Write-Host "🏦 Starting Indian Banking System..." -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Green

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1
    Write-Host "✅ Java version: $($javaVersion[0])" -ForegroundColor Green
} catch {
    Write-Host "❌ Error: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 17 or higher and try again" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if Maven is installed
try {
    $mavenVersion = mvn -version 2>&1
    Write-Host "✅ Maven version: $($mavenVersion[0])" -ForegroundColor Green
} catch {
    Write-Host "❌ Error: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven and try again" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if MongoDB is running (optional check)
$mongodProcess = Get-Process -Name "mongod" -ErrorAction SilentlyContinue
if (-not $mongodProcess) {
    Write-Host "⚠️  Warning: MongoDB is not running" -ForegroundColor Yellow
    Write-Host "Please start MongoDB before running the banking system" -ForegroundColor Yellow
    Write-Host "You can start MongoDB with: mongod --dbpath C:\data\db" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Or using Docker: docker run -d -p 27017:27017 --name mongodb mongo:latest" -ForegroundColor Yellow
    Write-Host ""
    $continue = Read-Host "Do you want to continue anyway? (y/N)"
    if ($continue -ne "y" -and $continue -ne "Y") {
        exit 1
    }
} else {
    Write-Host "✅ MongoDB is running" -ForegroundColor Green
}

# Set environment variables
$env:SPRING_PROFILES_ACTIVE = "development"
$env:JAVA_OPTS = "-Xmx512m -Xms256m"

# Create logs directory
if (-not (Test-Path "logs")) {
    New-Item -ItemType Directory -Path "logs" | Out-Null
}

Write-Host ""
Write-Host "🚀 Building and starting the banking system..." -ForegroundColor Cyan
Write-Host "==============================================" -ForegroundColor Cyan

# Navigate to backend directory
Set-Location "backend"

# Build the project
Write-Host "📦 Building project..." -ForegroundColor Yellow
& mvn clean install -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Build failed. Please check the errors above." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "✅ Build successful!" -ForegroundColor Green

# Start the application
Write-Host "🏃 Starting application..." -ForegroundColor Cyan
Write-Host ""
Write-Host "The banking system will start in CLI mode." -ForegroundColor White
Write-Host "Web interface will be available at: http://localhost:8080" -ForegroundColor White
Write-Host "API endpoints will be available at: http://localhost:8080/api" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Yellow
Write-Host ""

# Run the application
& java $env:JAVA_OPTS -jar target\banking-system-1.0.0.jar

Write-Host ""
Write-Host "👋 Banking system stopped. Thank you for using Indian Banking System!" -ForegroundColor Green
Read-Host "Press Enter to exit"
