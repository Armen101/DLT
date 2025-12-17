#!/bin/bash

echo "==================================="
echo "Dubai License Theory Backend Server"
echo "==================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed."
    echo "Please install Java 17 or higher:"
    echo "  - macOS: brew install openjdk@17"
    echo "  - Or download from: https://www.oracle.com/java/technologies/downloads/"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "Error: Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✓ Java version: $(java -version 2>&1 | head -n 1)"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo ""
    echo "Error: Maven is not installed."
    echo "Please install Maven:"
    echo "  - macOS: brew install maven"
    echo "  - Or download from: https://maven.apache.org/download.cgi"
    echo ""
    echo "Alternatively, you can run the application from IntelliJ IDEA:"
    echo "  1. Open the project in IntelliJ IDEA"
    echo "  2. Right-click on DubaiLicenseTheoryApplication.java"
    echo "  3. Select 'Run'"
    exit 1
fi

echo "✓ Maven version: $(mvn -version | head -n 1)"
echo ""

# Check if questions.json exists
if [ ! -f "src/main/resources/questions.json" ]; then
    echo "⚠ Warning: questions.json not found!"
    echo "Please add your questions file to: src/main/resources/questions.json"
    echo ""
    read -p "Continue anyway? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo "Building the project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "Build failed! Please check the errors above."
    exit 1
fi

echo ""
echo "Starting the application..."
echo ""
echo "==================================="
echo "Server will be available at:"
echo "  API: http://localhost:8080/api"
echo "  H2 Console: http://localhost:8080/h2-console"
echo ""
echo "Press Ctrl+C to stop the server"
echo "==================================="
echo ""

mvn spring-boot:run
