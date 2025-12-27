#!/bin/bash

# Deployment Verification Script
# This script tests your Railway deployment to ensure everything works

set -e  # Exit on error

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if URL is provided
if [ -z "$1" ]; then
    echo -e "${RED}Usage: ./verify-deployment.sh <your-railway-url>${NC}"
    echo -e "${YELLOW}Example: ./verify-deployment.sh https://your-app.railway.app${NC}"
    exit 1
fi

BASE_URL="$1"
echo -e "${YELLOW}Testing deployment at: ${BASE_URL}${NC}\n"

# Test counter
TESTS_PASSED=0
TESTS_FAILED=0

# Helper function to test endpoint
test_endpoint() {
    local name=$1
    local method=$2
    local endpoint=$3
    local data=$4
    local expected_status=$5

    echo -e "${YELLOW}Testing: ${name}${NC}"

    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" "${BASE_URL}${endpoint}")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "${BASE_URL}${endpoint}" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi

    # Extract status code (last line)
    status_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    if [ "$status_code" = "$expected_status" ]; then
        echo -e "${GREEN}✓ PASSED${NC} (Status: $status_code)"
        TESTS_PASSED=$((TESTS_PASSED + 1))
        if [ -n "$body" ] && [ "$body" != "null" ]; then
            echo "  Response: $(echo "$body" | head -c 100)..."
        fi
    else
        echo -e "${RED}✗ FAILED${NC} (Expected: $expected_status, Got: $status_code)"
        TESTS_FAILED=$((TESTS_FAILED + 1))
        if [ -n "$body" ]; then
            echo "  Response: $body"
        fi
    fi
    echo ""
}

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}  DEPLOYMENT VERIFICATION TESTS${NC}"
echo -e "${YELLOW}========================================${NC}\n"

# Test 1: Basic health check
echo -e "${YELLOW}[1/10] Testing Application Health...${NC}"
test_endpoint "Health Check" "GET" "/actuator/health" "" "200"

# Test 2: Get topics
echo -e "${YELLOW}[2/10] Testing Get Topics...${NC}"
test_endpoint "Get All Topics" "GET" "/api/questions/topics" "" "200"

# Test 3: Register new user
echo -e "${YELLOW}[3/10] Testing User Registration...${NC}"
RANDOM_EMAIL="test$(date +%s)@example.com"
REGISTER_DATA='{
  "fullName": "Test User",
  "email": "'$RANDOM_EMAIL'",
  "password": "Test123!",
  "phoneNumber": "+971501234567"
}'
test_endpoint "Register User" "POST" "/api/auth/register" "$REGISTER_DATA" "201"

# Test 4: Login with registered user
echo -e "${YELLOW}[4/10] Testing User Login...${NC}"
LOGIN_DATA='{
  "email": "'$RANDOM_EMAIL'",
  "password": "Test123!"
}'
login_response=$(curl -s -w "\n%{http_code}" -X POST "${BASE_URL}/api/auth/login" \
    -H "Content-Type: application/json" \
    -d "$LOGIN_DATA")

login_status=$(echo "$login_response" | tail -n1)
login_body=$(echo "$login_response" | sed '$d')

if [ "$login_status" = "200" ]; then
    echo -e "${GREEN}✓ PASSED${NC} (Status: $login_status)"
    TESTS_PASSED=$((TESTS_PASSED + 1))

    # Extract token
    TOKEN=$(echo "$login_body" | grep -o '"token":"[^"]*' | sed 's/"token":"//')
    USER_ID=$(echo "$login_body" | grep -o '"id":[0-9]*' | sed 's/"id"://')

    echo "  User ID: $USER_ID"
    echo "  Token: ${TOKEN:0:20}..."
else
    echo -e "${RED}✗ FAILED${NC} (Expected: 200, Got: $login_status)"
    TESTS_FAILED=$((TESTS_FAILED + 1))
    echo "  Response: $login_body"
    TOKEN=""
    USER_ID=""
fi
echo ""

# Test 5: Get questions (requires auth)
echo -e "${YELLOW}[5/10] Testing Get Random Questions...${NC}"
if [ -n "$TOKEN" ]; then
    questions_response=$(curl -s -w "\n%{http_code}" -X GET \
        "${BASE_URL}/api/questions/random?count=5&language=en" \
        -H "Authorization: Bearer $TOKEN")

    questions_status=$(echo "$questions_response" | tail -n1)
    questions_body=$(echo "$questions_response" | sed '$d')

    if [ "$questions_status" = "200" ]; then
        echo -e "${GREEN}✓ PASSED${NC} (Status: $questions_status)"
        TESTS_PASSED=$((TESTS_PASSED + 1))

        # Extract first question ID for later use
        QUESTION_ID=$(echo "$questions_body" | grep -o '"id":[0-9]*' | head -1 | sed 's/"id"://')
        echo "  Retrieved question ID: $QUESTION_ID"
    else
        echo -e "${RED}✗ FAILED${NC} (Expected: 200, Got: $questions_status)"
        TESTS_FAILED=$((TESTS_FAILED + 1))
        echo "  Response: $questions_body"
        QUESTION_ID=""
    fi
else
    echo -e "${RED}✗ SKIPPED${NC} (No auth token available)"
    TESTS_FAILED=$((TESTS_FAILED + 1))
    QUESTION_ID=""
fi
echo ""

# Test 6: Submit exam
echo -e "${YELLOW}[6/10] Testing Exam Submission...${NC}"
if [ -n "$TOKEN" ] && [ -n "$USER_ID" ] && [ -n "$QUESTION_ID" ]; then
    EXAM_DATA='{
      "userId": '$USER_ID',
      "examType": "MOCK",
      "timeTakenSeconds": 60,
      "answers": [
        {
          "questionId": '$QUESTION_ID',
          "selectedAnswer": "A"
        }
      ]
    }'

    exam_response=$(curl -s -w "\n%{http_code}" -X POST "${BASE_URL}/api/exams/submit" \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer $TOKEN" \
        -d "$EXAM_DATA")

    exam_status=$(echo "$exam_response" | tail -n1)
    exam_body=$(echo "$exam_response" | sed '$d')

    if [ "$exam_status" = "201" ]; then
        echo -e "${GREEN}✓ PASSED${NC} (Status: $exam_status)"
        TESTS_PASSED=$((TESTS_PASSED + 1))

        EXAM_ID=$(echo "$exam_body" | grep -o '"id":[0-9]*' | head -1 | sed 's/"id"://')
        echo "  Exam ID: $EXAM_ID"
    else
        echo -e "${RED}✗ FAILED${NC} (Expected: 201, Got: $exam_status)"
        TESTS_FAILED=$((TESTS_FAILED + 1))
        echo "  Response: $exam_body"
        EXAM_ID=""
    fi
else
    echo -e "${RED}✗ SKIPPED${NC} (Prerequisites not met)"
    TESTS_FAILED=$((TESTS_FAILED + 1))
    EXAM_ID=""
fi
echo ""

# Test 7: Get exam history
echo -e "${YELLOW}[7/10] Testing Get Exam History...${NC}"
if [ -n "$TOKEN" ] && [ -n "$USER_ID" ]; then
    test_endpoint "Get Exam History" "GET" "/api/exams/user/$USER_ID/history" "" "200"
else
    echo -e "${RED}✗ SKIPPED${NC} (No user ID available)"
    TESTS_FAILED=$((TESTS_FAILED + 1))
    echo ""
fi

# Test 8: Get user stats
echo -e "${YELLOW}[8/10] Testing Get User Stats...${NC}"
if [ -n "$TOKEN" ] && [ -n "$USER_ID" ]; then
    test_endpoint "Get User Stats" "GET" "/api/exams/user/$USER_ID/stats" "" "200"
else
    echo -e "${RED}✗ SKIPPED${NC} (No user ID available)"
    TESTS_FAILED=$((TESTS_FAILED + 1))
    echo ""
fi

# Test 9: Get exam result
echo -e "${YELLOW}[9/10] Testing Get Exam Result...${NC}"
if [ -n "$TOKEN" ] && [ -n "$EXAM_ID" ]; then
    test_endpoint "Get Exam Result" "GET" "/api/exams/result/$EXAM_ID" "" "200"
else
    echo -e "${RED}✗ SKIPPED${NC} (No exam ID available)"
    TESTS_FAILED=$((TESTS_FAILED + 1))
    echo ""
fi

# Test 10: Database persistence verification
echo -e "${YELLOW}[10/10] Testing Database Persistence...${NC}"
if [ -n "$TOKEN" ] && [ -n "$RANDOM_EMAIL" ]; then
    echo -e "${YELLOW}Verifying user still exists in database...${NC}"

    # Try to login again
    relogin_response=$(curl -s -w "\n%{http_code}" -X POST "${BASE_URL}/api/auth/login" \
        -H "Content-Type: application/json" \
        -d "$LOGIN_DATA")

    relogin_status=$(echo "$relogin_response" | tail -n1)

    if [ "$relogin_status" = "200" ]; then
        echo -e "${GREEN}✓ PASSED${NC} - User persists in database"
        TESTS_PASSED=$((TESTS_PASSED + 1))
        echo -e "${GREEN}  ✓ Database is correctly persisting data!${NC}"
    else
        echo -e "${RED}✗ FAILED${NC} - User not found after creation"
        TESTS_FAILED=$((TESTS_FAILED + 1))
        echo -e "${RED}  ✗ Database may not be persisting correctly${NC}"
    fi
else
    echo -e "${RED}✗ SKIPPED${NC} (User not created)"
    TESTS_FAILED=$((TESTS_FAILED + 1))
fi
echo ""

# Summary
echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}  TEST SUMMARY${NC}"
echo -e "${YELLOW}========================================${NC}"
echo -e "${GREEN}Tests Passed: ${TESTS_PASSED}${NC}"
echo -e "${RED}Tests Failed: ${TESTS_FAILED}${NC}"
echo -e "${YELLOW}Total Tests:  $((TESTS_PASSED + TESTS_FAILED))${NC}\n"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}  ✓ ALL TESTS PASSED!${NC}"
    echo -e "${GREEN}  Your deployment is working correctly!${NC}"
    echo -e "${GREEN}========================================${NC}"
    exit 0
else
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}  ✗ SOME TESTS FAILED${NC}"
    echo -e "${RED}  Please check the errors above${NC}"
    echo -e "${RED}========================================${NC}"
    exit 1
fi
