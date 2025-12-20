#!/bin/bash

API_URL="https://drivertheoryuae.com"

echo "========================================="
echo "Dubai License Theory API - Full Test"
echo "========================================="
echo ""

# Test public endpoints
echo "1. Root endpoint:"
curl -s "$API_URL/"
echo -e "\n"

echo "2. Health check:"
curl -s "$API_URL/health"
echo -e "\n"

echo "3. Get all topics:"
curl -s "$API_URL/api/questions/topics"
echo -e "\n"

# Test authentication
echo "4. Register a test user:"
curl -s -X POST "$API_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123!",
    "fullName": "Test User"
  }'
echo -e "\n"

echo "5. Login to get JWT token:"
LOGIN_RESPONSE=$(curl -s -X POST "$API_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test123!"
  }')
echo "$LOGIN_RESPONSE"
echo -e "\n"

# Extract token (if login successful)
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*' | cut -d'"' -f4)

if [ -n "$TOKEN" ]; then
  echo "6. Get random questions (authenticated):"
  curl -s "$API_URL/api/questions/random?count=3" \
    -H "Authorization: Bearer $TOKEN"
  echo -e "\n"

  echo "7. Get question by ID (authenticated):"
  curl -s "$API_URL/api/questions/1" \
    -H "Authorization: Bearer $TOKEN"
  echo -e "\n"

  echo "8. Get questions in Arabic (authenticated):"
  curl -s "$API_URL/api/questions/random?count=2&language=ar" \
    -H "Authorization: Bearer $TOKEN"
  echo -e "\n"
else
  echo "❌ Could not get authentication token. User might already exist or there's an auth issue."
  echo "Trying to login with existing credentials..."
fi

echo "========================================="
echo "Tests completed!"
echo "========================================="
