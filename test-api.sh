#!/bin/bash

# Dubai License Theory API Test Script
API_URL="https://zwcrxni9.up.railway.app"

echo "========================================"
echo "Dubai License Theory API Tests"
echo "========================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
PASSED=0
FAILED=0

# Function to test endpoint
test_endpoint() {
    local name=$1
    local url=$2
    local expected_status=${3:-200}

    echo -n "Testing: $name ... "
    response=$(curl -s -w "\n%{http_code}" "$url")
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n-1)

    if [ "$http_code" -eq "$expected_status" ]; then
        echo -e "${GREEN}✓ PASSED${NC} (HTTP $http_code)"
        echo "$body" | jq '.' 2>/dev/null || echo "$body"
        ((PASSED++))
    else
        echo -e "${RED}✗ FAILED${NC} (HTTP $http_code, expected $expected_status)"
        echo "$body"
        ((FAILED++))
    fi
    echo ""
}

# Test 1: Root endpoint
test_endpoint "Root endpoint" "$API_URL/"

# Test 2: Health check
test_endpoint "Health check" "$API_URL/health"

# Test 3: Get all questions
test_endpoint "Get all questions" "$API_URL/api/questions"

# Test 4: Get random questions
test_endpoint "Get random questions (5)" "$API_URL/api/questions/random?count=5"

# Test 5: Get all topics
test_endpoint "Get all topics" "$API_URL/api/questions/topics"

# Test 6: Get questions by difficulty
test_endpoint "Get easy questions" "$API_URL/api/questions/difficulty/easy"

# Test 7: Test with Arabic language
test_endpoint "Get questions in Arabic" "$API_URL/api/questions/random?count=3&language=ar"

# Test 8: Get question by ID
test_endpoint "Get question by ID (1)" "$API_URL/api/questions/1"

echo "========================================"
echo -e "Test Results: ${GREEN}$PASSED passed${NC}, ${RED}$FAILED failed${NC}"
echo "========================================"

exit $FAILED
