@echo off
REM Indian Banking System - Windows Startup Script
REM This script starts the banking system with proper configuration

echo 🏦 Starting Indian Banking System...
echo =====================================

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Java is not installed or not in PATH
    echo Please install Java 17 or higher and try again
    pause
    exit /b 1
)

echo ✅ Java version:
java -version

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

echo ✅ Maven version:
mvn -version

REM Check if MongoDB is running (optional check)
tasklist /FI "IMAGENAME eq mongod.exe" 2>NUL | find /I /N "mongod.exe">NUL
if %errorlevel% neq 0 (
    echo ⚠️  Warning: MongoDB is not running
    echo Please start MongoDB before running the banking system
    echo You can start MongoDB with: mongod --dbpath C:\data\db
    echo.
    echo Or using Docker: docker run -d -p 27017:27017 --name mongodb mongo:latest
    echo.
    set /p continue="Do you want to continue anyway? (y/N): "
    if /i not "%continue%"=="y" exit /b 1
) else (
    echo ✅ MongoDB is running
)

REM Set environment variables
set SPRING_PROFILES_ACTIVE=development
set JAVA_OPTS=-Xmx512m -Xms256m

REM Create logs directory
if not exist logs mkdir logs

echo.
echo 🚀 Building and starting the banking system...
echo ==============================================

REM Navigate to backend directory
cd backend

REM Build the project
echo 📦 Building project...
call mvn clean install -DskipTests

if %errorlevel% neq 0 (
    echo ❌ Build failed. Please check the errors above.
    pause
    exit /b 1
)

echo ✅ Build successful!

REM Start the application
echo 🏃 Starting application...
echo.
echo The banking system will start in CLI mode.
echo Web interface will be available at: http://localhost:8080
echo API endpoints will be available at: http://localhost:8080/api
echo.
echo Press Ctrl+C to stop the application
echo.

REM Run the application
java %JAVA_OPTS% -jar target\banking-system-1.0.0.jar

echo.
echo 👋 Banking system stopped. Thank you for using Indian Banking System!
pause
