# 🚀 START HERE - Quick Setup

## Problem: Dependencies Not Found?

If you're seeing errors like `package org.springframework.data.jpa.repository does not exist`

### ⚡ Quick Fix (30 seconds)

1. **Look at the RIGHT side** of IntelliJ → Click **Maven** tab
2. **Click the circular arrows icon** (↻) "Reload All Maven Projects"
3. **Wait 2-5 minutes** for download
4. **Done!** Errors should disappear

**Can't find Maven tab?**
→ Menu: View → Tool Windows → Maven

---

## Full Documentation

| Problem | Read This |
|---------|-----------|
| Dependencies not downloading | [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md) |
| Detailed dependency troubleshooting | [FIX_DEPENDENCIES.md](FIX_DEPENDENCIES.md) |
| How to setup and run | [SETUP.md](SETUP.md) |
| Quick start guide | [QUICKSTART.md](QUICKSTART.md) |
| API documentation | [README.md](README.md) |
| API examples | [API_EXAMPLES.md](API_EXAMPLES.md) |

---

## After Dependencies Download Successfully

### Step 1: Add Your Questions

Copy your 1500 questions JSON file to:
```
src/main/resources/questions.json
```

### Step 2: Run the Application

1. Open: `src/main/java/com/dubai/dlt/DubaiLicenseTheoryApplication.java`
2. Right-click → **Run**
3. Server starts at: **http://localhost:8080**

### Step 3: Test It

```bash
# Get all topics
curl http://localhost:8080/api/questions/topics

# Get 5 random questions
curl "http://localhost:8080/api/questions/random?count=5&language=en"
```

---

## Project Structure

```
DLT/
├── src/main/java/com/dubai/dlt/
│   ├── DubaiLicenseTheoryApplication.java  ← Run this file
│   ├── entity/          ← Database models
│   ├── repository/      ← Database access
│   ├── service/         ← Business logic
│   ├── controller/      ← REST API endpoints
│   ├── dto/             ← API request/response objects
│   └── config/          ← Configuration
│
├── src/main/resources/
│   ├── application.properties
│   └── questions.json   ← ADD YOUR 1500 QUESTIONS HERE
│
└── Documentation/
    ├── START_HERE.md          ← You are here
    ├── INTELLIJ_SETUP.md      ← IntelliJ setup guide
    ├── FIX_DEPENDENCIES.md    ← Dependency troubleshooting
    ├── QUICKSTART.md          ← Quick start guide
    ├── SETUP.md               ← Detailed setup
    ├── README.md              ← Full documentation
    └── API_EXAMPLES.md        ← API usage examples
```

---

## Quick Commands

### In IntelliJ Terminal

```bash
# Download dependencies (if Maven installed)
mvn dependency:resolve

# Or use our script
./download-dependencies.sh

# Run the application
mvn spring-boot:run
```

---

## Key Features Built

✅ Multi-language support (English, Arabic, Hindi/Urdu)
✅ User management with preferred language
✅ Random question selection for exams
✅ Topic and difficulty filtering
✅ Exam submission with auto-grading (80% pass)
✅ User statistics and progress tracking
✅ Complete REST API for mobile apps
✅ H2 database with auto-question loading

---

## API Base URL

```
http://localhost:8080/api
```

### Main Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/users` | POST | Create user |
| `/api/questions/random?count=20&language=ar` | GET | Get exam questions |
| `/api/questions/topics` | GET | Get all topics |
| `/api/exams/submit` | POST | Submit exam answers |
| `/api/exams/user/{id}/stats` | GET | Get user statistics |

See [API_EXAMPLES.md](API_EXAMPLES.md) for complete list with examples.

---

## Troubleshooting

### Maven not downloading?
→ Read [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)

### Dependencies still not found?
→ Read [FIX_DEPENDENCIES.md](FIX_DEPENDENCIES.md)

### Want to use command line?
→ Run `./download-dependencies.sh`

### Complete reset needed?
1. Close IntelliJ
2. Delete `.idea` and `target` folders
3. Reopen project
4. Import as Maven project

---

## Next Steps

1. ✅ Download dependencies (read above)
2. ✅ Add your questions.json file
3. ✅ Run the application
4. ✅ Test the API endpoints
5. ✅ Connect your mobile app

---

## Support

All documentation is included in this project:
- Start with [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md) for immediate help
- Read [QUICKSTART.md](QUICKSTART.md) for running the app
- Check [API_EXAMPLES.md](API_EXAMPLES.md) for API usage

---

**Most Common Issue:** Maven dependencies not downloaded
**Quick Solution:** Maven tab (right side) → Click reload icon (↻)
**Detailed Help:** [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)
