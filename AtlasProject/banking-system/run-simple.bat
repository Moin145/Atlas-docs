@echo off
REM Simple Banking System Runner - No Maven Required
REM This script runs the banking system using pre-compiled classes

echo 🏦 Starting Indian Banking System (Simple Mode)...
echo ================================================

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Java is not installed or not in PATH
    echo Please install Java 17 or higher and try again
    pause
    exit /b 1
)

echo ✅ Java is available

REM Set environment variables
set SPRING_PROFILES_ACTIVE=development
set JAVA_OPTS=-Xmx512m -Xms256m

REM Create logs directory
if not exist logs mkdir logs

echo.
echo 🚀 Starting the banking system...
echo =================================

REM Navigate to backend directory
cd backend

REM Check if we have a pre-built JAR
if exist "target\banking-system-1.0.0.jar" (
    echo ✅ Found pre-built JAR file
    echo 🏃 Starting application...
    echo.
    echo The banking system will start in CLI mode.
    echo Web interface will be available at: http://localhost:8080
    echo API endpoints will be available at: http://localhost:8080/api
    echo.
    echo Press Ctrl+C to stop the application
    echo.
    
    java %JAVA_OPTS% -jar target\banking-system-1.0.0.jar
) else (
    echo ❌ Pre-built JAR file not found
    echo.
    echo Please install Maven and run:
    echo   mvn clean install
    echo.
    echo Or use the full setup guide in WINDOWS_SETUP.md
    echo.
    pause
    exit /b 1
)

echo.
echo 👋 Banking system stopped. Thank you for using Indian Banking System!
pause
