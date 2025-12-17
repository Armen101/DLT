# Quick Start Guide

## What's Been Created

A complete Spring Boot REST API backend for your Dubai Driver License Theory exam application with:

- Multi-language support (English, Arabic, Hindi/Urdu)
- User management system
- Question bank with filtering and random selection
- Exam submission and grading
- Statistics and progress tracking
- H2 database for easy development

## Fastest Way to Run (Using IntelliJ)

Since you're already in IntelliJ IDEA:

### Step 1: Add Your Questions
1. Copy your 1500 questions JSON file
2. Paste it at: `src/main/resources/questions.json`

### Step 2: Run the Application
1. Open `src/main/java/com/dubai/dlt/DubaiLicenseTheoryApplication.java`
2. Right-click on the file
3. Select "Run 'DubaiLicenseTheoryApplication'"

That's it! The server will start at `http://localhost:8080`

## Alternative: Command Line

If you have Maven installed:
```bash
./run.sh
```

Or manually:
```bash
mvn clean install
mvn spring-boot:run
```

## Quick Test

Once running, test in your browser or terminal:

```bash
# Get all topics
curl http://localhost:8080/api/questions/topics

# Get 5 random questions in Arabic
curl "http://localhost:8080/api/questions/random?count=5&language=ar"
```

## Mobile App Integration

Your mobile app should connect to: `http://localhost:8080/api`

Key endpoints for your mobile app:

1. **Create User**: `POST /api/users`
2. **Get Questions**: `GET /api/questions/random?count=20&language=ar`
3. **Submit Exam**: `POST /api/exams/submit`
4. **Get Results**: `GET /api/exams/result/{id}`
5. **User Stats**: `GET /api/exams/user/{userId}/stats`

## Documentation Files

- `README.md` - Complete project documentation
- `SETUP.md` - Detailed setup instructions
- `API_EXAMPLES.md` - All API endpoints with examples
- `PROJECT_STRUCTURE.md` - Code organization

## Database

H2 database console (for debugging):
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/dlt_db`
- Username: `sa`
- Password: (leave empty)

## Important Notes

1. **Add Your Questions**: The app includes only 1 sample question. Add your 1500 questions to `src/main/resources/questions.json`

2. **CORS Enabled**: The API accepts requests from any origin (configured for development)

3. **Auto-Loading**: Questions are loaded automatically on first startup

4. **Language Support**:
   - `en` - English
   - `ar` - Arabic
   - `hi_ur` - Hindi/Urdu

5. **Passing Score**: 80% or higher

## Troubleshooting

### Maven not found?
Use IntelliJ's built-in Maven (it's already configured in your project)

### Port 8080 already in use?
Change port in `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Questions not loading?
Check that `questions.json` is in `src/main/resources/` and is valid JSON

## For Production

Before deploying to production:

1. Switch to MySQL/PostgreSQL (config in `application.properties`)
2. Restrict CORS origins (edit `WebConfig.java`)
3. Enable HTTPS
4. Use environment variables for sensitive data
5. Set `spring.jpa.hibernate.ddl-auto=validate`

## Need Help?

Check the documentation files:
- Setup issues → `SETUP.md`
- API usage → `API_EXAMPLES.md`
- Code structure → `PROJECT_STRUCTURE.md`
