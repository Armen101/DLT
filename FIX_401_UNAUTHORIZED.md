# Fix 401 Unauthorized Error After Deployment

## Problem

After deployment, you're getting 401 Unauthorized errors when trying to access API endpoints.

## Cause

Your application has JWT authentication enabled. The JWT filter checks for an `Authorization: Bearer <token>` header on all protected endpoints.

---

## Quick Diagnosis

**Which endpoint are you trying to access?**

Run this command to test different endpoints:

```bash
# Replace with your actual Railway URL
RAILWAY_URL="https://your-app.railway.app"

# Test 1: Health check (should work without auth)
curl -v $RAILWAY_URL/actuator/health

# Test 2: Topics (should work without auth)
curl -v $RAILWAY_URL/api/questions/topics

# Test 3: Register (should work without auth)
curl -v -X POST $RAILWAY_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test","email":"test@example.com","password":"Test123!","phoneNumber":"+971501234567"}'

# Test 4: Get questions (REQUIRES auth - will get 401 without token)
curl -v $RAILWAY_URL/api/questions/random?count=5&language=en
```

---

## Solutions Based on Endpoint

### Scenario 1: Getting 401 on Public Endpoints (auth, topics, health)

**This shouldn't happen.** Public endpoints are:
- `/api/auth/*` (register, login)
- `/api/questions/topics`
- `/actuator/*` (health, metrics)
- `/h2-console` (if enabled)

**If you're getting 401 on these:**

Check the deployment logs:

```bash
# In Railway: Your Service → Latest Deployment → View Logs
```

Look for errors like:
- JWT filter initialization errors
- Filter chain errors

**Fix:** The filter might be misconfigured. I've already updated it to allow `/actuator/*` endpoints.

---

### Scenario 2: Getting 401 on Protected Endpoints (questions, exams)

**This is expected!** These endpoints require authentication.

**Protected endpoints:**
- `/api/questions/*` (except `/topics`)
- `/api/exams/*`

**To access protected endpoints:**

1. **Get a token first:**
   ```bash
   # Register a user
   curl -X POST https://your-app.railway.app/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "fullName": "Test User",
       "email": "test@example.com",
       "password": "Test123!",
       "phoneNumber": "+971501234567"
     }'
   ```

   Response will include:
   ```json
   {
     "id": 1,
     "fullName": "Test User",
     "email": "test@example.com",
     "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdEBleGFtcGxlLmNvbSIsInN1YiI6InRlc3RAZXhhbXBsZS5jb20iLCJpYXQiOjE3MDMxNTQwMDAsImV4cCI6MTcwMzI0MDQwMH0.xxx"
   }
   ```

2. **Save the token**

3. **Use the token in subsequent requests:**
   ```bash
   TOKEN="your-token-here"

   curl https://your-app.railway.app/api/questions/random?count=5&language=en \
     -H "Authorization: Bearer $TOKEN"
   ```

---

### Scenario 3: Token Expired

JWT tokens expire after 24 hours.

**Error message:**
```json
{"error": "Invalid or expired token"}
```

**Fix:** Login again to get a new token:

```bash
curl -X POST https://your-app.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test123!"
  }'
```

---

### Scenario 4: CORS Issues (browser only)

If accessing from a web browser/frontend:

**Error in browser console:**
```
Access to fetch at 'https://...' from origin 'http://localhost:3000'
has been blocked by CORS policy
```

**Fix:** Your app already has CORS configured for all origins (`*`), but verify:

1. Check if you're using the correct headers
2. For browser requests, you may need to handle OPTIONS preflight:

```javascript
// In your frontend
fetch('https://your-app.railway.app/api/questions/random?count=5&language=en', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
```

---

## Complete Test Flow

Here's a complete test to verify authentication works:

```bash
RAILWAY_URL="https://your-app.railway.app"

# Step 1: Register
echo "1. Registering user..."
REGISTER_RESPONSE=$(curl -s -X POST $RAILWAY_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test'$(date +%s)'@example.com",
    "password": "Test123!",
    "phoneNumber": "+971501234567"
  }')

echo "Register response: $REGISTER_RESPONSE"

# Step 2: Extract token
TOKEN=$(echo $REGISTER_RESPONSE | grep -o '"token":"[^"]*' | sed 's/"token":"//')
echo "Token: ${TOKEN:0:20}..."

# Step 3: Test protected endpoint
echo ""
echo "2. Testing protected endpoint with token..."
curl -s $RAILWAY_URL/api/questions/random?count=5&language=en \
  -H "Authorization: Bearer $TOKEN" | head -c 200

echo ""
echo ""
echo "3. Testing protected endpoint WITHOUT token (should get 401)..."
curl -s $RAILWAY_URL/api/questions/random?count=5&language=en

echo ""
```

---

## Common Mistakes

### ❌ Mistake 1: Forgetting "Bearer " prefix

**Wrong:**
```bash
curl -H "Authorization: your-token"
```

**Correct:**
```bash
curl -H "Authorization: Bearer your-token"
```

### ❌ Mistake 2: Using wrong endpoint

**Wrong:** Trying to access `/api/questions/random` without auth

**Correct:** Login first, get token, then use token

### ❌ Mistake 3: Token in body instead of header

**Wrong:**
```bash
curl -X POST -d '{"token": "xxx"}'
```

**Correct:**
```bash
curl -H "Authorization: Bearer xxx"
```

---

## Public vs Protected Endpoints

### ✅ Public (No Auth Required)

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/questions/topics`
- `GET /actuator/health`

### 🔒 Protected (Auth Required)

- `GET /api/questions/random`
- `GET /api/questions/topic/{topic}`
- `GET /api/questions/{id}`
- `POST /api/exams/submit`
- `GET /api/exams/user/{userId}/history`
- `GET /api/exams/user/{userId}/stats`
- `GET /api/exams/result/{resultId}`

---

## Debugging Steps

1. **Check which endpoint is failing:**
   ```bash
   curl -v https://your-app.railway.app/your-endpoint
   ```
   Look for the HTTP status code

2. **Check response body:**
   Should show:
   ```json
   {"error": "Missing or invalid Authorization header"}
   ```
   or
   ```json
   {"error": "Invalid or expired token"}
   ```

3. **Check Railway logs:**
   - Railway → Your Service → Deployments → Latest → View Logs
   - Look for JWT validation errors

4. **Verify token is valid:**
   ```bash
   # Decode JWT (doesn't verify signature, just shows contents)
   echo "your-token" | cut -d. -f2 | base64 -d 2>/dev/null | jq
   ```

   Should show:
   ```json
   {
     "userId": 1,
     "username": "test@example.com",
     "sub": "test@example.com",
     "iat": 1703154000,
     "exp": 1703240400
   }
   ```

---

## Solution Applied

I've already updated `JwtAuthenticationFilter.java` to allow `/actuator/*` endpoints without authentication.

**Commit and deploy:**

```bash
git add src/main/java/com/dubai/dlt/config/JwtAuthenticationFilter.java
git commit -m "Allow actuator endpoints without auth"
git push
```

---

## Still Getting 401?

If you're still getting 401 after trying the above:

1. **Tell me which specific endpoint** you're trying to access
2. **Share the exact curl command** you're using
3. **Share the error response** you're getting
4. **Check Railway logs** for any JWT errors

I'll help you debug further!
