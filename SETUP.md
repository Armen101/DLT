# Setup Instructions

## Prerequisites

1. Install Java 17 or higher
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Or use: `brew install openjdk@17` (macOS with Homebrew)

2. Install Maven
   - Download from: https://maven.apache.org/download.cgi
   - Or use: `brew install maven` (macOS with Homebrew)

## Setup Steps

### 1. Add Your Questions Data

Place your 1500 questions JSON file at:
```
src/main/resources/questions.json
```

The file should contain an array of question objects with the structure shown in the sample.

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or after building, run the JAR:
```bash
java -jar target/driver-license-theory-1.0.0.jar
```

### 4. Access the Application

- API Base URL: `http://localhost:8080/api`
- H2 Database Console: `http://localhost:8080/h2-console`

## Quick Test

Once the application is running, test with:

```bash
# Get all topics
curl http://localhost:8080/api/questions/topics

# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "fullName": "Test User",
    "preferredLanguage": "en"
  }'

# Get random questions
curl "http://localhost:8080/api/questions/random?count=5&language=en"
```

## Using IntelliJ IDEA

Since you're already using IntelliJ:

1. Open the project in IntelliJ IDEA
2. Wait for Maven to download dependencies
3. Add your questions.json file to src/main/resources/
4. Right-click on `DubaiLicenseTheoryApplication.java`
5. Select "Run 'DubaiLicenseTheoryApplication'"

The application will start and load your questions automatically.

## Troubleshooting

### Maven Not Found
If Maven is not installed, install it using:
- macOS: `brew install maven`
- Windows: Download from https://maven.apache.org/download.cgi
- Linux: `sudo apt-get install maven` or `sudo yum install maven`

### Port Already in Use
If port 8080 is already in use, change it in `application.properties`:
```properties
server.port=8081
```

### Questions Not Loading
Ensure your questions.json file:
- Is located at `src/main/resources/questions.json`
- Contains valid JSON
- Follows the structure shown in the README

## Production Deployment

For production use:

1. Change database from H2 to MySQL/PostgreSQL
2. Update `application.properties` with production database credentials
3. Configure CORS settings to restrict origins
4. Enable HTTPS
5. Set appropriate logging levels
6. Use environment variables for sensitive configuration

Example for MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dlt_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
