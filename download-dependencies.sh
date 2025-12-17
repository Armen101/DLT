#!/bin/bash

echo "======================================"
echo "Maven Dependency Downloader"
echo "======================================"
echo ""

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    echo "Error: pom.xml not found!"
    echo "Please run this script from the project root directory."
    exit 1
fi

echo "Found pom.xml ✓"
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "Error: Java not found!"
    exit 1
fi

echo "Java version: $(java -version 2>&1 | head -n 1)"
echo ""

# Check if Maven is available
if command -v mvn &> /dev/null; then
    echo "Maven found ✓"
    echo "Maven version: $(mvn -version | head -n 1)"
    echo ""
    echo "Downloading dependencies..."
    echo "This may take 2-5 minutes..."
    echo ""

    mvn dependency:resolve dependency:resolve-plugins -U

    if [ $? -eq 0 ]; then
        echo ""
        echo "======================================"
        echo "✓ Dependencies downloaded successfully!"
        echo "======================================"
        echo ""
        echo "Next steps:"
        echo "1. Go back to IntelliJ IDEA"
        echo "2. Click File → Reload All from Disk"
        echo "3. Or close and reopen the project"
        echo ""
        echo "The errors should now be gone!"
    else
        echo ""
        echo "Failed to download dependencies."
        echo "Please check your internet connection."
    fi
else
    echo "Maven not found on command line."
    echo ""
    echo "Please use IntelliJ IDEA instead:"
    echo "1. Open the Maven tool window (View → Tool Windows → Maven)"
    echo "2. Click the 'Reload All Maven Projects' icon (circular arrows)"
    echo "3. Wait for dependencies to download"
    echo ""
    echo "OR install Maven:"
    echo "  brew install maven"
fi
