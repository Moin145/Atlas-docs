#!/bin/bash

# Indian Banking System - Startup Script
# This script starts the banking system with proper configuration

echo "🏦 Starting Indian Banking System..."
echo "====================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher and try again"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Error: Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Error: Maven is not installed or not in PATH"
    echo "Please install Maven and try again"
    exit 1
fi

echo "✅ Maven version: $(mvn -version | head -n 1)"

# Check if MongoDB is running
if ! pgrep -x "mongod" > /dev/null; then
    echo "⚠️  Warning: MongoDB is not running"
    echo "Please start MongoDB before running the banking system"
    echo "You can start MongoDB with: mongod --dbpath /data/db"
    echo ""
    echo "Or using Docker: docker run -d -p 27017:27017 --name mongodb mongo:latest"
    echo ""
    read -p "Do you want to continue anyway? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
else
    echo "✅ MongoDB is running"
fi

# Set environment variables
export SPRING_PROFILES_ACTIVE=development
export JAVA_OPTS="-Xmx512m -Xms256m"

# Create logs directory
mkdir -p logs

echo ""
echo "🚀 Building and starting the banking system..."
echo "=============================================="

# Navigate to backend directory
cd backend

# Build the project
echo "📦 Building project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the errors above."
    exit 1
fi

echo "✅ Build successful!"

# Start the application
echo "🏃 Starting application..."
echo ""
echo "The banking system will start in CLI mode."
echo "Web interface will be available at: http://localhost:8080"
echo "API endpoints will be available at: http://localhost:8080/api"
echo ""
echo "Press Ctrl+C to stop the application"
echo ""

# Run the application
java $JAVA_OPTS -jar target/banking-system-1.0.0.jar

echo ""
echo "👋 Banking system stopped. Thank you for using Indian Banking System!"
