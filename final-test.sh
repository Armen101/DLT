#!/bin/bash

API_URL="https://drivertheoryuae.com"

echo "========================================="
echo "Dubai License Theory API - Complete Test"
echo "========================================="
echo ""

# Test 1: Root endpoint
echo "✓ Test 1: Root endpoint"
curl -s "$API_URL/" | python3 -m json.tool
echo ""

# Test 2: Health check
echo "✓ Test 2: Health check"
curl -s "$API_URL/health" | python3 -m json.tool
echo ""

# Test 3: Get all topics
echo "✓ Test 3: Get all topics"
curl -s "$API_URL/api/questions/topics" | python3 -m json.tool
echo ""

# Test 4: Register a new user
echo "✓ Test 4: Register new user"
TIMESTAMP=$(date +%s)
REGISTER_RESPONSE=$(curl -s -X POST "$API_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"testuser$TIMESTAMP\",
    \"email\": \"test$TIMESTAMP@example.com\",
    \"password\": \"Test123!\",
    \"fullName\": \"Test User\",
    \"preferredLanguage\": \"en\"
  }")
echo "$REGISTER_RESPONSE" | python3 -m json.tool
echo ""

# Test 5: Login
echo "✓ Test 5: Login with new user"
LOGIN_RESPONSE=$(curl -s -X POST "$API_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"testuser$TIMESTAMP\",
    \"password\": \"Test123!\"
  }")
echo "$LOGIN_RESPONSE" | python3 -m json.tool
echo ""

# Extract token
TOKEN=$(echo "$LOGIN_RESPONSE" | python3 -c "import sys, json; print(json.load(sys.stdin).get('token', ''))" 2>/dev/null)

if [ -n "$TOKEN" ] && [ "$TOKEN" != "null" ]; then
  echo "✓ Authentication successful! Token obtained."
  echo ""
  
  # Test 6: Get random questions
  echo "✓ Test 6: Get 3 random questions (English)"
  curl -s "$API_URL/api/questions/random?count=3" \
    -H "Authorization: Bearer $TOKEN" | python3 -m json.tool | head -50
  echo ""

  # Test 7: Get question by ID
  echo "✓ Test 7: Get question by ID (1)"
  curl -s "$API_URL/api/questions/1" \
    -H "Authorization: Bearer $TOKEN" | python3 -m json.tool
  echo ""

  # Test 8: Get questions in Arabic
  echo "✓ Test 8: Get 2 random questions (Arabic)"
  curl -s "$API_URL/api/questions/random?count=2&language=ar" \
    -H "Authorization: Bearer $TOKEN" | python3 -m json.tool | head -50
  echo ""

  # Test 9: Get questions by topic
  echo "✓ Test 9: Get questions by topic (Signs)"
  curl -s "$API_URL/api/questions/topic/Signs?language=en" \
    -H "Authorization: Bearer $TOKEN" | python3 -m json.tool | head -30
  echo ""

  # Test 10: Get questions by difficulty
  echo "✓ Test 10: Get easy questions"
  curl -s "$API_URL/api/questions/difficulty/easy?language=en" \
    -H "Authorization: Bearer $TOKEN" | python3 -m json.tool | head -30
  echo ""

else
  echo "❌ Authentication failed. Could not obtain token."
fi

echo "========================================="
echo "All tests completed!"
echo "API URL: $API_URL"
echo "========================================="
