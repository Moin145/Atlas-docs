#!/bin/bash

# Banking System - Batch Settlement Script
# This script processes queued transactions for settlement
# Should be run as a cron job (e.g., daily at 2 AM)

# Configuration
JAVA_HOME="/usr/lib/jvm/java-17-openjdk"
APP_HOME="/opt/banking-system"
LOG_DIR="/var/log/banking-system"
JAR_FILE="$APP_HOME/banking-system-1.0.0.jar"

# Create log directory if it doesn't exist
mkdir -p $LOG_DIR

# Log file with timestamp
LOG_FILE="$LOG_DIR/settlement-$(date +%Y%m%d-%H%M%S).log"

# Function to log messages
log_message() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a $LOG_FILE
}

# Function to send email notification (optional)
send_notification() {
    local subject="$1"
    local message="$2"
    
    # Uncomment and configure if you want email notifications
    # echo "$message" | mail -s "$subject" admin@bankingsystem.com
    log_message "NOTIFICATION: $subject - $message"
}

# Start settlement process
log_message "=========================================="
log_message "Starting Banking System Settlement Process"
log_message "=========================================="

# Check if Java is available
if ! command -v java &> /dev/null; then
    log_message "ERROR: Java is not installed or not in PATH"
    send_notification "Settlement Failed" "Java is not available"
    exit 1
fi

# Check if JAR file exists
if [ ! -f "$JAR_FILE" ]; then
    log_message "ERROR: JAR file not found: $JAR_FILE"
    send_notification "Settlement Failed" "JAR file not found"
    exit 1
fi

# Set Java options
JAVA_OPTS="-Xmx512m -Xms256m"
JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=production"
JAVA_OPTS="$JAVA_OPTS -Dlogging.file.name=$LOG_DIR/banking-system.log"

# MongoDB connection (adjust as needed)
export SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/banking_system"

# DynamoDB configuration (adjust as needed)
export AWS_REGION="ap-south-1"
export AWS_ACCESS_KEY_ID="your-access-key"
export AWS_SECRET_ACCESS_KEY="your-secret-key"

log_message "Java version: $(java -version 2>&1 | head -n 1)"
log_message "JAR file: $JAR_FILE"
log_message "Log file: $LOG_FILE"

# Run settlement process
log_message "Starting settlement process..."

# Execute the settlement
java $JAVA_OPTS -jar $JAR_FILE --spring.main.class=com.bankingsystem.SettlementRunner 2>&1 | tee -a $LOG_FILE

# Capture exit code
EXIT_CODE=${PIPESTATUS[0]}

# Check if settlement was successful
if [ $EXIT_CODE -eq 0 ]; then
    log_message "Settlement process completed successfully"
    send_notification "Settlement Completed" "Daily settlement process completed successfully"
else
    log_message "ERROR: Settlement process failed with exit code $EXIT_CODE"
    send_notification "Settlement Failed" "Daily settlement process failed with exit code $EXIT_CODE"
fi

# Clean up old log files (keep last 30 days)
find $LOG_DIR -name "settlement-*.log" -mtime +30 -delete 2>/dev/null

log_message "Settlement process finished"
log_message "=========================================="

exit $EXIT_CODE
