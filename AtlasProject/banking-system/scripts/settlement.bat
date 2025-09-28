@echo off
REM Banking System - Windows Batch Settlement Script
REM This script processes queued transactions for settlement
REM Should be run as a scheduled task (e.g., daily at 2 AM)

REM Configuration
set JAVA_HOME=C:\Program Files\Java\jdk-17
set APP_HOME=C:\opt\banking-system
set LOG_DIR=C:\var\log\banking-system
set JAR_FILE=%APP_HOME%\banking-system-1.0.0.jar

REM Create log directory if it doesn't exist
if not exist %LOG_DIR% mkdir %LOG_DIR%

REM Log file with timestamp
for /f "tokens=2 delims==" %%a in ('wmic OS Get localdatetime /value') do set "dt=%%a"
set "YY=%dt:~2,2%" & set "YYYY=%dt:~0,4%" & set "MM=%dt:~4,2%" & set "DD=%dt:~6,2%"
set "HH=%dt:~8,2%" & set "Min=%dt:~10,2%" & set "Sec=%dt:~12,2%"
set "LOG_FILE=%LOG_DIR%\settlement-%YYYY%%MM%%DD%-%HH%%Min%%Sec%.log"

REM Function to log messages
echo [%date% %time%] %1 >> %LOG_FILE%

REM Start settlement process
echo [%date% %time%] ========================================== >> %LOG_FILE%
echo [%date% %time%] Starting Banking System Settlement Process >> %LOG_FILE%
echo [%date% %time%] ========================================== >> %LOG_FILE%

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [%date% %time%] ERROR: Java is not installed or not in PATH >> %LOG_FILE%
    echo [%date% %time%] NOTIFICATION: Settlement Failed - Java is not available >> %LOG_FILE%
    exit /b 1
)

REM Check if JAR file exists
if not exist "%JAR_FILE%" (
    echo [%date% %time%] ERROR: JAR file not found: %JAR_FILE% >> %LOG_FILE%
    echo [%date% %time%] NOTIFICATION: Settlement Failed - JAR file not found >> %LOG_FILE%
    exit /b 1
)

REM Set Java options
set JAVA_OPTS=-Xmx512m -Xms256m
set JAVA_OPTS=%JAVA_OPTS% -Dspring.profiles.active=production
set JAVA_OPTS=%JAVA_OPTS% -Dlogging.file.name=%LOG_DIR%\banking-system.log

REM MongoDB connection (adjust as needed)
set SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/banking_system

REM DynamoDB configuration (adjust as needed)
set AWS_REGION=ap-south-1
set AWS_ACCESS_KEY_ID=your-access-key
set AWS_SECRET_ACCESS_KEY=your-secret-key

echo [%date% %time%] Java version: >> %LOG_FILE%
java -version >> %LOG_FILE% 2>&1
echo [%date% %time%] JAR file: %JAR_FILE% >> %LOG_FILE%
echo [%date% %time%] Log file: %LOG_FILE% >> %LOG_FILE%

REM Run settlement process
echo [%date% %time%] Starting settlement process... >> %LOG_FILE%

REM Execute the settlement
java %JAVA_OPTS% -jar %JAR_FILE% --spring.main.class=com.bankingsystem.SettlementRunner >> %LOG_FILE% 2>&1

REM Capture exit code
set EXIT_CODE=%errorlevel%

REM Check if settlement was successful
if %EXIT_CODE% equ 0 (
    echo [%date% %time%] Settlement process completed successfully >> %LOG_FILE%
    echo [%date% %time%] NOTIFICATION: Settlement Completed - Daily settlement process completed successfully >> %LOG_FILE%
) else (
    echo [%date% %time%] ERROR: Settlement process failed with exit code %EXIT_CODE% >> %LOG_FILE%
    echo [%date% %time%] NOTIFICATION: Settlement Failed - Daily settlement process failed with exit code %EXIT_CODE% >> %LOG_FILE%
)

REM Clean up old log files (keep last 30 days)
forfiles /p %LOG_DIR% /m settlement-*.log /d -30 /c "cmd /c del @path" 2>nul

echo [%date% %time%] Settlement process finished >> %LOG_FILE%
echo [%date% %time%] ========================================== >> %LOG_FILE%

exit /b %EXIT_CODE%
