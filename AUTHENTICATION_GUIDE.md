# Authentication Guide

## Overview

The system now has complete authentication with username and password:
- User registration with password
- Login endpoint
- Password validation

## Authentication Endpoints

### 1. Register a New User

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "username": "ahmed123",
  "email": "ahmed@example.com",
  "password": "password123",
  "fullName": "Ahmed Al Maktoum",
  "preferredLanguage": "ar"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Registration successful",
  "user": {
    "id": 1,
    "username": "ahmed123",
    "email": "ahmed@example.com",
    "fullName": "Ahmed Al Maktoum",
    "preferredLanguage": "ar"
  }
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Username already exists",
  "user": null
}
```

### 2. Login

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "ahmed123",
  "password": "password123"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 1,
    "username": "ahmed123",
    "email": "ahmed@example.com",
    "fullName": "Ahmed Al Maktoum",
    "preferredLanguage": "ar"
  }
}
```

**Error Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid username or password",
  "user": null
}
```

## Validation Rules

### Registration
- **Username**:
  - Required
  - 3-50 characters
  - Must be unique
- **Email**:
  - Required
  - Valid email format
  - Must be unique
- **Password**:
  - Required
  - Minimum 6 characters
- **Preferred Language**:
  - Required
  - Values: "en", "ar", "hi_ur"

### Login
- **Username**: Required
- **Password**: Required

## Testing Authentication

### Using curl

**Register:**
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

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123"
  }'
```

### Using IntelliJ HTTP Client

Open `api-tests.http` and run the authentication requests at the top!

## Mobile App Integration Flow

### 1. User Registration Flow
```
Mobile App → POST /api/auth/register
          ← {success: true, user: {...}}

Store user.id and user data locally
Redirect to home screen
```

### 2. User Login Flow
```
Mobile App → POST /api/auth/login
          ← {success: true, user: {...}}

Store user.id and user data locally
Update last login timestamp
Redirect to home screen
```

### 3. Failed Login Flow
```
Mobile App → POST /api/auth/login
          ← {success: false, message: "Invalid username or password"}

Show error message to user
Keep on login screen
```

## Complete User Journey

### New User Registration

1. **User opens mobile app**
2. **Clicks "Register"**
3. **Fills registration form:**
   - Username: "ahmed123"
   - Email: "ahmed@example.com"
   - Password: "password123"
   - Full Name: "Ahmed Al Maktoum"
   - Preferred Language: Arabic
4. **App sends POST request to `/api/auth/register`**
5. **Server responds with success and user data**
6. **App stores user ID locally**
7. **User is logged in and redirected to home**

### Returning User Login

1. **User opens mobile app**
2. **Sees login screen**
3. **Enters credentials:**
   - Username: "ahmed123"
   - Password: "password123"
4. **App sends POST request to `/api/auth/login`**
5. **Server validates credentials**
6. **Updates last_login timestamp**
7. **Returns user data**
8. **User is logged in**

### Take Exam Flow (After Login)

1. **User is logged in (has user.id)**
2. **Selects "Start Practice Exam"**
3. **App requests questions:**
   ```
   GET /api/questions/random?count=20&language=ar
   ```
4. **User answers questions**
5. **App submits exam:**
   ```
   POST /api/exams/submit
   {
     "userId": 1,
     "examType": "practice",
     "answers": [...]
   }
   ```
6. **Server grades and returns results**
7. **App shows results to user**

## Security Notes

**Important:** This is a basic authentication system for development. For production, you should:

1. **Hash passwords** - Don't store plain text passwords
   - Use BCrypt or similar
   - Passwords are currently stored as plain text

2. **Add JWT tokens** - For session management
   - Generate token on login
   - Send token with each request
   - Validate token on server

3. **Add refresh tokens** - For long-lived sessions

4. **Rate limiting** - Prevent brute force attacks

5. **HTTPS only** - Encrypt data in transit

## Current Implementation

✅ User registration with password
✅ Login with username/password validation
✅ Unique username and email validation
✅ Last login tracking
✅ Error handling
✅ Input validation

⚠️ **Not Production Ready:**
- Passwords stored as plain text (should be hashed)
- No session management (should use JWT)
- No rate limiting
- No password reset functionality

## Database Schema Update

The `users` table now includes:
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,  -- NEW FIELD
  full_name VARCHAR(255),
  preferred_language VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  last_login TIMESTAMP
);
```

## Next Steps for Production

If you want to make this production-ready, I can add:

1. **Password hashing with BCrypt**
2. **JWT token generation and validation**
3. **Password reset via email**
4. **Account verification**
5. **Remember me functionality**
6. **Session management**

Let me know if you want any of these features!
