# Project Structure

```
DLT/
├── pom.xml                                 # Maven configuration
├── run.sh                                  # Startup script
├── README.md                               # Project documentation
├── SETUP.md                                # Setup instructions
├── API_EXAMPLES.md                         # API testing examples
├── PROJECT_STRUCTURE.md                    # This file
│
├── src/
│   ├── main/
│   │   ├── java/com/dubai/dlt/
│   │   │   ├── DubaiLicenseTheoryApplication.java    # Main application
│   │   │   │
│   │   │   ├── entity/                    # Database entities
│   │   │   │   ├── User.java             # User model
│   │   │   │   ├── Question.java         # Question model
│   │   │   │   ├── ExamResult.java       # Exam result model
│   │   │   │   └── UserAnswer.java       # User answer tracking
│   │   │   │
│   │   │   ├── repository/                # Data access layer
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── QuestionRepository.java
│   │   │   │   ├── ExamResultRepository.java
│   │   │   │   └── UserAnswerRepository.java
│   │   │   │
│   │   │   ├── service/                   # Business logic layer
│   │   │   │   ├── UserService.java
│   │   │   │   ├── QuestionService.java
│   │   │   │   └── ExamService.java
│   │   │   │
│   │   │   ├── controller/                # REST API controllers
│   │   │   │   ├── UserController.java
│   │   │   │   ├── QuestionController.java
│   │   │   │   └── ExamController.java
│   │   │   │
│   │   │   ├── dto/                       # Data Transfer Objects
│   │   │   │   ├── UserDTO.java
│   │   │   │   ├── QuestionDTO.java
│   │   │   │   ├── ExamSubmissionDTO.java
│   │   │   │   ├── ExamResultDTO.java
│   │   │   │   └── UserStatsDTO.java
│   │   │   │
│   │   │   └── config/                    # Configuration classes
│   │   │       ├── WebConfig.java         # CORS configuration
│   │   │       ├── GlobalExceptionHandler.java  # Error handling
│   │   │       └── DataLoader.java        # Question data loader
│   │   │
│   │   └── resources/
│   │       ├── application.properties     # Application configuration
│   │       └── questions.json             # Question data (add your 1500 questions here)
│   │
│   └── test/
│       └── java/com/dubai/dlt/           # Test classes (future)
│
└── data/                                  # H2 database files (auto-generated)
    └── dlt_db.mv.db
```

## Key Components

### Entities (Database Models)

1. **User** - User accounts with preferred language
2. **Question** - Multi-language question bank
3. **ExamResult** - Exam submissions with scores
4. **UserAnswer** - Individual answer tracking

### Repositories

Spring Data JPA repositories for database operations with custom queries for:
- Random question selection
- Topic and difficulty filtering
- User statistics calculation

### Services

Business logic layer handling:
- User management
- Question retrieval with language support
- Exam submission and grading
- Statistics calculation

### Controllers

REST API endpoints for:
- User CRUD operations
- Question queries (random, by topic, by difficulty)
- Exam submission and results
- User statistics

### DTOs

Data transfer objects for clean API contracts:
- Hide sensitive data
- Language-specific question formatting
- Validation

### Configuration

- CORS for mobile app access
- Global exception handling
- Automatic question data loading

## Database Schema

### users
- id (PK)
- username (unique)
- email (unique)
- full_name
- preferred_language
- created_at
- last_login

### questions
- id (PK)
- topic_tag
- difficulty
- question_en, question_ar, question_hi_ur
- a_en, b_en, c_en, d_en
- a_ar, b_ar, c_ar, d_ar
- a_hi_ur, b_hi_ur, c_hi_ur, d_hi_ur
- correct
- explanation_en, explanation_ar, explanation_hi_ur

### exam_results
- id (PK)
- user_id (FK)
- total_questions
- correct_answers
- score_percentage
- exam_type
- time_taken_seconds
- passed
- created_at

### user_answers
- id (PK)
- user_id (FK)
- question_id (FK)
- exam_result_id (FK)
- selected_answer
- is_correct
- answered_at

## API Endpoints Summary

### Users
- POST /api/users - Create user
- GET /api/users/{id} - Get user
- GET /api/users/username/{username} - Get by username
- PUT /api/users/{id} - Update user
- POST /api/users/{id}/login - Update last login

### Questions
- GET /api/questions - All questions
- GET /api/questions/{id} - Single question
- GET /api/questions/topic/{topic} - By topic
- GET /api/questions/difficulty/{difficulty} - By difficulty
- GET /api/questions/random - Random questions
- GET /api/questions/random/topic/{topic} - Random by topic
- GET /api/questions/topics - All topics

### Exams
- POST /api/exams/submit - Submit exam
- GET /api/exams/user/{userId}/history - Exam history
- GET /api/exams/result/{resultId} - Exam result details
- GET /api/exams/user/{userId}/stats - User statistics

## Technologies

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: H2 (development), MySQL-ready (production)
- **Build Tool**: Maven
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Spring Validation
- **JSON**: Jackson

## Next Steps

1. Add your 1500 questions to `src/main/resources/questions.json`
2. Run the application (see SETUP.md)
3. Test endpoints (see API_EXAMPLES.md)
4. Connect your mobile app to `http://localhost:8080/api`
