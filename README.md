# Dubai Driver License Theory - Backend API

Backend REST API for Dubai Driver Theory License Exam Helper application.

## Features

- Multi-language support (English, Arabic, Hindi/Urdu)
- User management
- Question bank with 1500+ questions
- Exam submission and result tracking
- User statistics and performance tracking
- Random question selection
- Topic and difficulty filtering

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (development)
- Maven

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository
2. Navigate to project directory
3. Place your questions JSON file at `src/main/resources/questions.json`
4. Build and run the application

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Database Console

H2 Database console is available at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/dlt_db`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### User Management

#### Create User
```http
POST /api/users
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "fullName": "John Doe",
  "preferredLanguage": "en"
}
```

#### Get User by ID
```http
GET /api/users/{id}
```

#### Get User by Username
```http
GET /api/users/username/{username}
```

#### Update User
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "fullName": "John Smith",
  "preferredLanguage": "ar"
}
```

#### Update Last Login
```http
POST /api/users/{id}/login
```

### Questions

#### Get All Questions
```http
GET /api/questions?language=en
```

#### Get Question by ID
```http
GET /api/questions/{id}?language=en
```

#### Get Questions by Topic
```http
GET /api/questions/topic/{topic}?language=en
```

#### Get Questions by Difficulty
```http
GET /api/questions/difficulty/{difficulty}?language=en
```

#### Get Random Questions
```http
GET /api/questions/random?count=20&language=en
```

#### Get Random Questions by Topic
```http
GET /api/questions/random/topic/{topic}?count=20&language=en
```

#### Get All Topics
```http
GET /api/questions/topics
```

### Exam Management

#### Submit Exam
```http
POST /api/exams/submit
Content-Type: application/json

{
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
    }
  ]
}
```

#### Get User Exam History
```http
GET /api/exams/user/{userId}/history
```

#### Get Exam Result by ID
```http
GET /api/exams/result/{resultId}
```

#### Get User Statistics
```http
GET /api/exams/user/{userId}/stats
```

## Supported Languages

- `en` - English (default)
- `ar` - Arabic
- `hi_ur` - Hindi/Urdu

## Question Data Format

Place your questions JSON file at `src/main/resources/questions.json` with the following structure:

```json
[
  {
    "topic_tag": "Signs",
    "difficulty": "Easy",
    "question_en": "Question text in English",
    "question_ar": "Question text in Arabic",
    "question_hi_ur": "Question text in Hindi/Urdu",
    "A_en": "Option A in English",
    "B_en": "Option B in English",
    "C_en": "Option C in English",
    "D_en": "Option D in English",
    "A_ar": "Option A in Arabic",
    "B_ar": "Option B in Arabic",
    "C_ar": "Option C in Arabic",
    "D_ar": "Option D in Arabic",
    "A_hi_ur": "Option A in Hindi/Urdu",
    "B_hi_ur": "Option B in Hindi/Urdu",
    "C_hi_ur": "Option C in Hindi/Urdu",
    "D_hi_ur": "Option D in Hindi/Urdu",
    "correct": "B",
    "explanation_en": "Explanation in English",
    "explanation_ar": "Explanation in Arabic",
    "explanation_hi_ur": "Explanation in Hindi/Urdu"
  }
]
```

## Database Schema

### Tables

- `users` - User accounts
- `questions` - Question bank
- `exam_results` - Exam submissions and scores
- `user_answers` - Individual answer tracking

## Development Notes

- CORS is enabled for all origins (configure for production)
- Passing score is 80%
- Questions are loaded automatically on first startup
- H2 database is stored in `./data/dlt_db.mv.db`

## Configuration

Edit `src/main/resources/application.properties` to customize:
- Server port
- Database configuration
- Logging levels
- Timezone settings

## License

Private project for Dubai Driver License Theory application.
