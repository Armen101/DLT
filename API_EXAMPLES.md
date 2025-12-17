# API Examples and Testing

## Base URL
```
http://localhost:8080/api
```

## User Management APIs

### 1. Create a New User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ahmed_dubai",
    "email": "ahmed@example.com",
    "fullName": "Ahmed Al Maktoum",
    "preferredLanguage": "ar"
  }'
```

Response:
```json
{
  "id": 1,
  "username": "ahmed_dubai",
  "email": "ahmed@example.com",
  "fullName": "Ahmed Al Maktoum",
  "preferredLanguage": "ar"
}
```

### 2. Get User by ID
```bash
curl http://localhost:8080/api/users/1
```

### 3. Get User by Username
```bash
curl http://localhost:8080/api/users/username/ahmed_dubai
```

### 4. Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Ahmed Al Maktoum Updated",
    "preferredLanguage": "en"
  }'
```

### 5. Update Last Login
```bash
curl -X POST http://localhost:8080/api/users/1/login
```

## Question APIs

### 1. Get All Topics
```bash
curl http://localhost:8080/api/questions/topics
```

Response:
```json
["Signs", "Traffic Rules", "Road Safety", "Parking"]
```

### 2. Get Random Questions (English)
```bash
curl "http://localhost:8080/api/questions/random?count=20&language=en"
```

### 3. Get Random Questions (Arabic)
```bash
curl "http://localhost:8080/api/questions/random?count=20&language=ar"
```

### 4. Get Random Questions (Hindi/Urdu)
```bash
curl "http://localhost:8080/api/questions/random?count=20&language=hi_ur"
```

### 5. Get Questions by Topic
```bash
curl "http://localhost:8080/api/questions/topic/Signs?language=en"
```

### 6. Get Questions by Difficulty
```bash
curl "http://localhost:8080/api/questions/difficulty/Easy?language=en"
```

### 7. Get Random Questions by Topic
```bash
curl "http://localhost:8080/api/questions/random/topic/Traffic%20Rules?count=10&language=ar"
```

### 8. Get Specific Question by ID
```bash
curl "http://localhost:8080/api/questions/1?language=en"
```

Response:
```json
{
  "id": 1,
  "topicTag": "Signs",
  "difficulty": "Easy",
  "question": "In heavy traffic, you see a GIVE WAY sign. What should you do?",
  "optionA": "Proceed only if the road is clear and it is safe.",
  "optionB": "Slow down and be prepared to stop if necessary.",
  "optionC": "Stop completely at the stop line, then proceed only when it is safe.",
  "optionD": "Increase your following distance and reduce speed.",
  "explanation": "Follow the instruction of the GIVE WAY sign to control traffic safely."
}
```

## Exam APIs

### 1. Submit Exam
```bash
curl -X POST http://localhost:8080/api/exams/submit \
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

Response:
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

### 2. Get User Exam History
```bash
curl http://localhost:8080/api/exams/user/1/history
```

Response:
```json
[
  {
    "id": 2,
    "userId": 1,
    "totalQuestions": 20,
    "correctAnswers": 18,
    "scorePercentage": 90.0,
    "examType": "final",
    "timeTakenSeconds": 1800,
    "passed": true,
    "createdAt": "2024-01-15T12:00:00",
    "questionResults": null
  },
  {
    "id": 1,
    "userId": 1,
    "totalQuestions": 20,
    "correctAnswers": 15,
    "scorePercentage": 75.0,
    "examType": "practice",
    "timeTakenSeconds": 1500,
    "passed": false,
    "createdAt": "2024-01-14T10:00:00",
    "questionResults": null
  }
]
```

### 3. Get Exam Result by ID
```bash
curl http://localhost:8080/api/exams/result/1
```

### 4. Get User Statistics
```bash
curl http://localhost:8080/api/exams/user/1/stats
```

Response:
```json
{
  "userId": 1,
  "totalExams": 5,
  "passedExams": 3,
  "averageScore": 82.5,
  "totalQuestionsAnswered": 100
}
```

## Exam Types

Common exam types you can use:
- `practice` - Practice exam
- `mock` - Mock exam
- `final` - Final exam
- `topic` - Topic-specific exam
- `quick` - Quick quiz

## Language Codes

- `en` - English
- `ar` - Arabic
- `hi_ur` - Hindi/Urdu

## Mobile App Integration Flow

### Typical User Flow:

1. **User Registration/Login**
   ```
   POST /api/users
   ```

2. **Update Last Login**
   ```
   POST /api/users/{id}/login
   ```

3. **Get Available Topics**
   ```
   GET /api/questions/topics
   ```

4. **Start Practice Exam (Get Random Questions)**
   ```
   GET /api/questions/random?count=20&language=ar
   ```

5. **Submit Exam**
   ```
   POST /api/exams/submit
   ```

6. **View Results**
   ```
   GET /api/exams/result/{resultId}
   ```

7. **Check Progress**
   ```
   GET /api/exams/user/{userId}/stats
   ```

8. **Review History**
   ```
   GET /api/exams/user/{userId}/history
   ```

## Testing with Postman

Import these endpoints into Postman:

1. Create a new Collection: "Dubai License Theory API"
2. Set base URL variable: `{{baseUrl}}` = `http://localhost:8080/api`
3. Create folders for: Users, Questions, Exams
4. Add the above endpoints to respective folders

## Error Responses

### Validation Error (400)
```json
{
  "username": "Username is required",
  "email": "Email should be valid"
}
```

### Not Found (400)
```json
{
  "error": "User not found with id: 999"
}
```

### Server Error (500)
```json
{
  "error": "An unexpected error occurred: ..."
}
```
