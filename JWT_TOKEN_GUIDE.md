# JWT Token Authentication Guide

## Overview

The system now uses **JWT (JSON Web Token)** for secure authentication. When users log in or register, they receive a token that must be included in subsequent API requests.

## How It Works

### 1. User Registration/Login
```
User → POST /api/auth/register or /api/auth/login
     ← Response includes JWT token
```

### 2. Making Authenticated Requests
```
Client → GET /api/questions/random
         Header: Authorization: Bearer <token>
      ← Response with data
```

### 3. Token Validation
```
Server checks:
- Is token present in Authorization header?
- Is token format valid?
- Is token expired?
- Does token signature match?
```

## API Endpoints

### Public Endpoints (No Token Required)

✅ **POST** `/api/auth/register` - Register new user
✅ **POST** `/api/auth/login` - Login
✅ **GET** `/api/questions/topics` - Get all topics

### Protected Endpoints (Token Required)

🔒 **GET** `/api/questions/random` - Get random questions
🔒 **GET** `/api/questions/{id}` - Get question by ID
🔒 **GET** `/api/questions/topic/{topic}` - Get questions by topic
🔒 **POST** `/api/exams/submit` - Submit exam
🔒 **GET** `/api/exams/user/{userId}/history` - Get exam history
🔒 **GET** `/api/users/{id}` - Get user details

## Login/Register Response

**Request:**
```bash
POST /api/auth/login
{
  "username": "ahmed123",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWhtZWQxMjMiLCJzdWIiOiJhaG1lZDEyMyIsImlhdCI6MTcwNDEwMDAwMCwiZXhwIjoxNzA0MTg2NDAwfQ.xyz123...",
  "user": {
    "id": 1,
    "username": "ahmed123",
    "email": "ahmed@example.com",
    "fullName": "Ahmed Al Maktoum",
    "preferredLanguage": "ar"
  }
}
```

## Using the Token

### Format

All authenticated requests must include the token in the `Authorization` header:

```
Authorization: Bearer <token>
```

### Example: Get Random Questions

**Request:**
```bash
GET /api/questions/random?count=10&language=ar
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW...
```

**With curl:**
```bash
curl -X GET "http://localhost:8080/api/questions/random?count=10&language=ar" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Example: Submit Exam

**Request:**
```bash
POST /api/exams/submit
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW...
Content-Type: application/json

{
  "userId": 1,
  "examType": "practice",
  "timeTakenSeconds": 1200,
  "answers": [...]
}
```

## Token Details

### Token Contents

The JWT contains:
- **userId**: User's ID
- **username**: User's username
- **iat**: Issued at timestamp
- **exp**: Expiration timestamp

### Token Expiration

- **Lifetime**: 24 hours
- **After expiration**: User must login again to get a new token

### Token Security

- Tokens are signed with HMAC-SHA256
- Secret key is stored on server
- Tampering with token will make it invalid

## Error Responses

### Missing Token (401 Unauthorized)
```json
{
  "error": "Missing or invalid Authorization header"
}
```

### Invalid Token (401 Unauthorized)
```json
{
  "error": "Invalid or expired token"
}
```

### Expired Token (401 Unauthorized)
```json
{
  "error": "Token validation failed"
}
```

## Mobile App Integration

### Complete Flow

**1. User Registers/Logs In:**
```javascript
// Login request
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    username: 'ahmed123',
    password: 'password123'
  })
});

const data = await response.json();

if (data.success) {
  // Store token locally (AsyncStorage, localStorage, etc.)
  await storage.setItem('authToken', data.token);
  await storage.setItem('user', JSON.stringify(data.user));
}
```

**2. Making Authenticated Requests:**
```javascript
// Get token from storage
const token = await storage.getItem('authToken');

// Make authenticated request
const response = await fetch('http://localhost:8080/api/questions/random?count=20&language=ar', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});

const questions = await response.json();
```

**3. Handle Token Expiration:**
```javascript
const response = await fetch(url, {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

if (response.status === 401) {
  // Token expired or invalid
  // Clear stored token
  await storage.removeItem('authToken');
  await storage.removeItem('user');

  // Redirect to login screen
  navigation.navigate('Login');
}
```

## Testing with IntelliJ HTTP Client

### Step 1: Login
```http
### Login to get token
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "ahmed123",
  "password": "password123"
}
```

### Step 2: Copy the Token

From the response, copy the `token` value.

### Step 3: Use Token in Requests
```http
### Get random questions with token
GET http://localhost:8080/api/questions/random?count=5&language=ar
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW...
```

## Testing with curl

### 1. Login and Save Token
```bash
# Login and get token
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ahmed123","password":"password123"}' \
  | jq -r '.token')

echo "Token: $TOKEN"
```

### 2. Use Token in Requests
```bash
# Get random questions
curl -X GET "http://localhost:8080/api/questions/random?count=5&language=ar" \
  -H "Authorization: Bearer $TOKEN"

# Submit exam
curl -X POST http://localhost:8080/api/exams/submit \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "examType": "practice",
    "timeTakenSeconds": 1200,
    "answers": [
      {"questionId": 1, "selectedAnswer": "B"}
    ]
  }'
```

## Testing with Postman

### 1. Create Login Request
- Method: POST
- URL: `http://localhost:8080/api/auth/login`
- Body → raw → JSON:
  ```json
  {
    "username": "ahmed123",
    "password": "password123"
  }
  ```
- Send and copy the `token` from response

### 2. Create Authenticated Request
- Method: GET
- URL: `http://localhost:8080/api/questions/random?count=5&language=ar`
- Headers tab:
  - Key: `Authorization`
  - Value: `Bearer <paste_token_here>`
- Send

### 3. Use Environment Variables (Pro Tip)
In Postman, you can save the token to an environment variable:

**In login request → Tests tab:**
```javascript
var jsonData = pm.response.json();
pm.environment.set("authToken", jsonData.token);
```

**In other requests → Headers:**
```
Authorization: Bearer {{authToken}}
```

## Security Best Practices

### For Mobile Apps

1. **Store tokens securely**
   - Use secure storage (Keychain on iOS, Keystore on Android)
   - Don't store in plain text

2. **Handle token expiration**
   - Implement automatic logout on 401
   - Show login screen when token expires

3. **Never expose tokens**
   - Don't log tokens to console
   - Don't include in error messages
   - Don't pass in URL parameters

4. **Use HTTPS in production**
   - Tokens should only be sent over HTTPS
   - Never use HTTP in production

### Token Lifecycle

```
Registration/Login → Receive Token → Store Token
                                         ↓
                                    Use Token for Requests
                                         ↓
                             Token Expires (24 hours)
                                         ↓
                                    Show Login Screen
                                         ↓
                                    Get New Token
```

## Common Issues

### Issue: Token not working
**Solution:** Check that you're including "Bearer " prefix
```
✅ Correct: Authorization: Bearer eyJhbGciOi...
❌ Wrong:   Authorization: eyJhbGciOi...
```

### Issue: 401 Unauthorized
**Solutions:**
1. Token may be expired (login again)
2. Token format is incorrect
3. Authorization header is missing

### Issue: Token expired quickly
**Solution:** Token lifetime is 24 hours. If testing, you may need to login multiple times.

## Production Considerations

For production deployment, consider:

1. **Refresh Tokens** - Allow long-lived sessions
2. **Token Revocation** - Ability to invalidate tokens
3. **Rate Limiting** - Prevent token abuse
4. **HTTPS Only** - Encrypt token transmission
5. **Shorter Expiration** - More secure, less convenient
6. **Password Hashing** - Use BCrypt (currently plain text)

## Summary

✅ JWT tokens generated on login/register
✅ Tokens valid for 24 hours
✅ Include token in `Authorization: Bearer <token>` header
✅ Public endpoints don't require token
✅ Protected endpoints return 401 without valid token
✅ Token contains userId and username
✅ Mobile apps should store token securely

**Next Steps:**
1. Login/register to get a token
2. Store token in your mobile app
3. Include token in all API requests
4. Handle 401 responses by redirecting to login
