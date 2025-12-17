#!/bin/bash

echo "=========================================="
echo "IntelliJ Maven Project Reset"
echo "=========================================="
echo ""
echo "This will reset your IntelliJ configuration"
echo "so the project is properly recognized as a Maven project."
echo ""
read -p "Continue? (y/n) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Cancelled."
    exit 1
fi

echo ""
echo "Step 1: Removing IntelliJ configuration files..."
rm -rf .idea
rm -rf *.iml
rm -rf target
rm -rf out

if [ $? -eq 0 ]; then
    echo "✓ Configuration files removed"
else
    echo "✗ Failed to remove files"
    exit 1
fi

echo ""
echo "=========================================="
echo "✓ Reset Complete!"
echo "=========================================="
echo ""
echo "Next steps:"
echo ""
echo "1. Open IntelliJ IDEA"
echo ""
echo "2. Click: File → Open"
echo ""
echo "3. Navigate to:"
echo "   /Users/armengevorgyan/IdeaProjects/DLT"
echo ""
echo "4. Select the DLT folder and click Open"
echo ""
echo "5. When you see 'Maven project detected' or"
echo "   'Import Maven project' notification:"
echo "   → Click 'Import' or 'Import Changes'"
echo ""
echo "6. Wait for indexing to complete"
echo ""
echo "7. Look for the Maven tool window on the RIGHT side"
echo ""
echo "8. Click the Reload icon (↻) in Maven window"
echo ""
echo "9. Wait 2-5 minutes for dependencies to download"
echo ""
echo "10. Done! You should see all dependencies loaded"
echo ""
echo "=========================================="
