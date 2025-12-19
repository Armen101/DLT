# API Testing Examples

## Base URL
```
http://localhost:8080/api
```

---

## 🔐 STEP 1: Get Your Token First!

Before testing protected endpoints, you need to login and get a token.

### Option A: Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "test123",
    "fullName": "Test User",
    "preferredLanguage": "en"
  }'
```

### Option B: Login with Existing User

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123"
  }'
```

**Response (copy the token value!):**
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwNDEwMDAwMCwiZXhwIjoxNzA0MTg2NDAwfQ.xyz...",
  "user": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "fullName": "Test User",
    "preferredLanguage": "en"
  }
}
```

### 💡 Save Your Token

**In Terminal (Linux/Mac):**
```bash
# Copy the token from response and save it
export TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWhtZWQxMjMiLCJzdWIiOiJhaG1lZDEyMyIsImlhdCI6MTc2NjExMjk3NiwiZXhwIjoxNzY2MTk5Mzc2fQ.aCb7JzV5yEAsPyqkDBmLZzW6rAm7o2ISUnY_C7TNH-c"

# Or login and auto-save token in one command:
export TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123"}' \
  -s | grep -o '"token":"[^"]*' | cut -d'"' -f4)

# Verify token is saved
echo $TOKEN
```

**In PowerShell (Windows):**
```powershell
# Save token
$TOKEN = "your-token-here"

# Verify
echo $TOKEN
```

---

## 📝 STEP 2: Use Token in Requests

Now you can use `$TOKEN` (or `%TOKEN%` on Windows) in all your requests!

---

## Authentication Endpoints (Public - No Token Needed)

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ahmed123",
    "email": "ahmed@example.com",
    "password": "password123",
    "fullName": "Ahmed Al Maktoum",
    "preferredLanguage": "ar"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ahmed123",
    "password": "password123"
  }'
```

---

## Question Endpoints

### Get All Topics (Public - No Token)
```bash
curl http://localhost:8080/api/questions/topics
```

### Get Random Questions (Protected - Token Required) ⭐
```bash
curl -X GET "http://localhost:8080/api/questions/random?count=5&language=en" \
  -H "Authorization: Bearer $TOKEN"
```

**With hardcoded token:**
```bash
curl -X GET "http://localhost:8080/api/questions/random?count=5&language=en" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Get Random Questions in Arabic
```bash
curl -X GET "http://localhost:8080/api/questions/random?count=10&language=ar" \
  -H "Authorization: Bearer $TOKEN"
```

### Get Random Questions in Hindi/Urdu
```bash
curl -X GET "http://localhost:8080/api/questions/random?count=10&language=hi_ur" \
  -H "Authorization: Bearer $TOKEN"
```

### Get Questions by Topic
```bash
curl -X GET "http://localhost:8080/api/questions/topic/Signs?language=en" \
  -H "Authorization: Bearer $TOKEN"
```

### Get Questions by Difficulty
```bash
curl -X GET "http://localhost:8080/api/questions/difficulty/Easy?language=en" \
  -H "Authorization: Bearer $TOKEN"
```

### Get Random Questions by Topic
```bash
curl -X GET "http://localhost:8080/api/questions/random/topic/Signs?count=5&language=ar" \
  -H "Authorization: Bearer $TOKEN"
```

### Get Specific Question by ID
```bash
curl -X GET "http://localhost:8080/api/questions/1?language=en" \
  -H "Authorization: Bearer $TOKEN"
```

### Get All Questions (might be large!)
```bash
curl -X GET "http://localhost:8080/api/questions?language=en" \
  -H "Authorization: Bearer $TOKEN"
```

---

## User Management Endpoints (Protected)

### Get User by ID
```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Get User by Username
```bash
curl -X GET http://localhost:8080/api/users/username/testuser \
  -H "Authorization: Bearer $TOKEN"
```

### Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Updated Name",
    "preferredLanguage": "ar"
  }'
```

### Update Last Login
```bash
curl -X POST http://localhost:8080/api/users/1/login \
  -H "Authorization: Bearer $TOKEN"
```

### Get All Users
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN"
```

---

## Exam Endpoints (Protected)

### Submit Exam ⭐
```bash
curl -X POST http://localhost:8080/api/exams/submit \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "examType": "practice",
    "timeTakenSeconds": 1200,
    "answers": [
      {
        "questionId": 1,
        "selectedAnswer": "B"
      },
      {
        "questionId": 2,
        "selectedAnswer": "A"
      },
      {
        "questionId": 3,
        "selectedAnswer": "C"
      }
    ]
  }'
```

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "totalQuestions": 3,
  "correctAnswers": 2,
  "scorePercentage": 66.67,
  "examType": "practice",
  "timeTakenSeconds": 1200,
  "passed": false,
  "createdAt": "2024-01-15T10:30:00",
  "questionResults": [
    {
      "questionId": 1,
      "selectedAnswer": "B",
      "correctAnswer": "B",
      "isCorrect": true
    },
    {
      "questionId": 2,
      "selectedAnswer": "A",
      "correctAnswer": "B",
      "isCorrect": false
    },
    {
      "questionId": 3,
      "selectedAnswer": "C",
      "correctAnswer": "C",
      "isCorrect": true
    }
  ]
}
```

### Get User Exam History
```bash
curl -X GET http://localhost:8080/api/exams/user/1/history \
  -H "Authorization: Bearer $TOKEN"
```

### Get Specific Exam Result
```bash
curl -X GET http://localhost:8080/api/exams/result/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Get User Statistics
```bash
curl -X GET http://localhost:8080/api/exams/user/1/stats \
  -H "Authorization: Bearer $TOKEN"
```

**Response:**
```json
{
  "userId": 1,
  "totalExams": 5,
  "passedExams": 3,
  "averageScore": 82.5,
  "totalQuestionsAnswered": 100
}
```

---

## Complete Workflow Example

### 1. Register/Login
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "student1",
    "email": "student1@example.com",
    "password": "pass123",
    "fullName": "Student One",
    "preferredLanguage": "en"
  }'

# Save the token from response
export TOKEN="paste-token-here"
```

### 2. Get Random Questions for Practice
```bash
curl -X GET "http://localhost:8080/api/questions/random?count=20&language=en" \
  -H "Authorization: Bearer $TOKEN"
```

### 3. Submit Practice Exam
```bash
curl -X POST http://localhost:8080/api/exams/submit \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "examType": "practice",
    "timeTakenSeconds": 1800,
    "answers": [
      {"questionId": 1, "selectedAnswer": "A"},
      {"questionId": 2, "selectedAnswer": "B"},
      {"questionId": 3, "selectedAnswer": "C"}
    ]
  }'
```

### 4. Check Statistics
```bash
curl -X GET http://localhost:8080/api/exams/user/1/stats \
  -H "Authorization: Bearer $TOKEN"
```

---

## Testing in Postman

### Setup
1. Create a new request
2. Go to **Headers** tab
3. Add key: `Authorization`
4. Add value: `Bearer YOUR_TOKEN_HERE`

### Environment Variable (Recommended)
1. Create environment in Postman
2. Add variable: `authToken`
3. In login request, go to **Tests** tab:
   ```javascript
   var jsonData = pm.response.json();
   pm.environment.set("authToken", jsonData.token);
   ```
4. In other requests, use: `Authorization: Bearer {{authToken}}`

---

## Testing in IntelliJ HTTP Client

Open `api-tests.http` file and click the green run arrows!

Or create your own test file:

```http
### Login and get token
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "test123"
}

### Save the token from above response, then use it below

### Get random questions
GET http://localhost:8080/api/questions/random?count=5&language=en
Authorization: Bearer YOUR_TOKEN_HERE

### Submit exam
POST http://localhost:8080/api/exams/submit
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "userId": 1,
  "examType": "practice",
  "timeTakenSeconds": 900,
  "answers": [
    {"questionId": 1, "selectedAnswer": "B"},
    {"questionId": 2, "selectedAnswer": "A"}
  ]
}
```

---

## Error Responses

### 401 Unauthorized (Missing Token)
```json
{
  "error": "Missing or invalid Authorization header"
}
```

### 401 Unauthorized (Invalid Token)
```json
{
  "error": "Invalid or expired token"
}
```

### 400 Bad Request (Login Failed)
```json
{
  "success": false,
  "message": "Invalid username or password"
}
```

---

## Quick Reference

### Public Endpoints (No Token)
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/questions/topics`

### Protected Endpoints (Token Required)
- All `/api/questions/*` (except topics)
- All `/api/exams/*`
- All `/api/users/*`

### Token Format
```
Authorization: Bearer YOUR_TOKEN_HERE
```

### Token Lifetime
- **24 hours**
- After expiration, login again to get new token

---

## Tips

1. **Always login first** to get your token
2. **Save the token** in a variable (`export TOKEN=...`)
3. **Include Authorization header** in all protected endpoints
4. **Don't forget "Bearer "** prefix before the token
5. **Token expires in 24 hours** - login again if you get 401

---

## Next Steps

1. ✅ Login/Register to get token
2. ✅ Save token in environment variable
3. ✅ Test protected endpoints with token
4. ✅ Integrate with your mobile app

**All examples are ready to copy and paste!** 🚀
