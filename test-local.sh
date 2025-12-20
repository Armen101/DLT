#!/bin/bash

# Test the API locally or on Railway
# Usage: ./test-local.sh [URL]
# Default: http://localhost:8080

API_URL="${1:-http://localhost:8080}"

echo "========================================"
echo "Testing API at: $API_URL"
echo "========================================"
echo ""

# Test 1: Root endpoint
echo "1. Testing Root endpoint (/):"
curl -s "$API_URL/" | jq '.' || curl -s "$API_URL/"
echo -e "\n"

# Test 2: Health check
echo "2. Testing Health check (/health):"
curl -s "$API_URL/health" | jq '.' || curl -s "$API_URL/health"
echo -e "\n"

# Test 3: Get all topics
echo "3. Testing Get all topics (/api/questions/topics):"
curl -s "$API_URL/api/questions/topics" | jq '.' || curl -s "$API_URL/api/questions/topics"
echo -e "\n"

# Test 4: Get random questions
echo "4. Testing Get 3 random questions (/api/questions/random?count=3):"
curl -s "$API_URL/api/questions/random?count=3" | jq '.[0:3]' || curl -s "$API_URL/api/questions/random?count=3"
echo -e "\n"

# Test 5: Get question by ID
echo "5. Testing Get question by ID (/api/questions/1):"
curl -s "$API_URL/api/questions/1" | jq '.' || curl -s "$API_URL/api/questions/1"
echo -e "\n"

# Test 6: Get questions in Arabic
echo "6. Testing Get questions in Arabic (/api/questions/random?count=2&language=ar):"
curl -s "$API_URL/api/questions/random?count=2&language=ar" | jq '.[0]' || curl -s "$API_URL/api/questions/random?count=2&language=ar"
echo -e "\n"

echo "========================================"
echo "Tests completed!"
echo "========================================"
