i n  od ath# 🪟 Windows Setup Guide for Indian Banking System

## Why the Error Occurred

The error you encountered happened because:
1. **`chmod`** is a Unix/Linux command, not available in Windows PowerShell
2. **`./start.sh`** is a shell script format, but Windows uses different execution methods
3. Windows PowerShell has different syntax and commands

## ✅ Solution: Windows-Compatible Scripts

I've created Windows-compatible versions for you:

### 1. **start.bat** - Windows Batch File
```cmd
start.bat
```

### 2. **start.ps1** - PowerShell Script
```powershell
.\start.ps1
```

## 🚀 Quick Start on Windows

### Option 1: Using Batch File (Recommended)
```cmd
# Double-click start.bat or run in Command Prompt
start.bat
```

### Option 2: Using PowerShell
```powershell
# Right-click and "Run with PowerShell" or run in PowerShell
.\start.ps1
```

### Option 3: Manual Build and Run
```cmd
# Open Command Prompt or PowerShell
cd banking-system\backend
mvn clean install
mvn spring-boot:run
```

## 📋 Prerequisites for Windows

### 1. Install Java 17+
- Download from: https://adoptium.net/
- Install and add to PATH
- Verify: `java -version`

### 2. Install Maven
- Download from: https://maven.apache.org/download.cgi
- Extract and add to PATH
- Verify: `mvn -version`

### 3. Install MongoDB
- Download from: https://www.mongodb.com/try/download/community
- Install and start service
- Or use Docker: `docker run -d -p 27017:27017 --name mongodb mongo:latest`

### 4. Configure AWS DynamoDB (Optional)
- Install AWS CLI
- Configure credentials
- Create DynamoDB table

## 🔧 Windows-Specific Configuration

### Environment Variables
The scripts automatically set:
- `SPRING_PROFILES_ACTIVE=development`
- `JAVA_OPTS=-Xmx512m -Xms256m`

### File Paths
- Uses Windows path separators (`\` instead of `/`)
- Creates logs directory: `C:\var\log\banking-system`
- JAR file location: `target\banking-system-1.0.0.jar`

## 🎯 What Each Script Does

### start.bat
- Checks Java and Maven installation
- Verifies MongoDB is running
- Builds the project with Maven
- Starts the Spring Boot application
- Provides user-friendly error messages

### start.ps1
- Same functionality as start.bat
- Better error handling
- Colored output
- More interactive prompts

## 🚨 Troubleshooting

### Common Issues:

1. **"Java is not recognized"**
   - Install Java 17+ and add to PATH
   - Restart Command Prompt/PowerShell

2. **"Maven is not recognized"**
   - Install Maven and add to PATH
   - Restart Command Prompt/PowerShell

3. **"MongoDB connection failed"**
   - Start MongoDB service
   - Or use Docker: `docker run -d -p 27017:27017 --name mongodb mongo:latest`

4. **"Port 8080 already in use"**
   - Stop other applications using port 8080
   - Or change port in application.properties

5. **"Build failed"**
   - Check internet connection (Maven downloads dependencies)
   - Ensure Java and Maven are properly installed

## 🎉 Success Indicators

When everything works correctly, you'll see:
```
🏦 Starting Indian Banking System...
=====================================
✅ Java version: openjdk version "17.0.x"
✅ Maven version: Apache Maven 3.x.x
✅ MongoDB is running
📦 Building project...
✅ Build successful!
🏃 Starting application...
```

## 📱 Access Points

Once running:
- **CLI Interface**: Interactive menu in terminal
- **Web Interface**: http://localhost:8080
- **API Endpoints**: http://localhost:8080/api

## 🔄 Next Steps

1. **Run the application**: Use `start.bat` or `.\start.ps1`
2. **Test the CLI**: Follow the interactive menu
3. **Test the Web UI**: Open http://localhost:8080
4. **Test the API**: Use Postman or curl to test endpoints

## 💡 Pro Tips

- **Use PowerShell** for better error messages and colored output
- **Run as Administrator** if you encounter permission issues
- **Check Windows Firewall** if you can't access the web interface
- **Use Docker** for MongoDB if you don't want to install it locally

The system is now fully Windows-compatible! 🎉
