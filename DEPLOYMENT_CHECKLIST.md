# Deployment Verification Checklist

## How to Verify Everything is Working

### Method 1: Automated Script (Recommended)

Run the verification script:

```bash
./verify-deployment.sh https://your-app.railway.app
```

Replace `https://your-app.railway.app` with your actual Railway URL.

**What it tests:**
- ✓ Application health
- ✓ User registration
- ✓ User login
- ✓ Get questions
- ✓ Submit exam
- ✓ Get exam results
- ✓ **Database persistence** (most important!)

---

### Method 2: Manual Verification

#### Step 1: Check Railway Deployment Status

**In Railway Dashboard:**

1. Go to your project
2. Click on your service
3. Click "Deployments" tab

**Look for:**
- ✅ Status: "Success" (green checkmark)
- ✅ Build completed successfully
- ✅ No error messages

**View Logs:**
Click on the latest deployment → View logs

**Good signs in logs:**
```
✓ HikariCP - Starting connection pool
✓ HHH000400: Using dialect: org.hibernate.dialect.PostgreSQLDialect
✓ Started DubaiLicenseTheoryApplication in X seconds
```

**Bad signs in logs:**
```
✗ Unable to obtain JDBC Connection
✗ Connection refused
✗ Using H2Dialect (means PostgreSQL not connected!)
```

---

#### Step 2: Test Application Endpoints

**Get your Railway URL:**
- Railway → Your Service → Settings → Domain
- Should be like: `https://your-app-name.up.railway.app`

**Test 1: Health Check**

```bash
curl https://your-app.railway.app/actuator/health
```

Expected: `{"status":"UP"}`

---

**Test 2: Get Topics**

```bash
curl https://your-app.railway.app/api/questions/topics
```

Expected: List of topics like `["SpeedDistance","AdverseWeather",...]`

---

**Test 3: Register User**

```bash
curl -X POST https://your-app.railway.app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test123@example.com",
    "password": "Test123!",
    "phoneNumber": "+971501234567"
  }'
```

Expected:
```json
{
  "id": 1,
  "fullName": "Test User",
  "email": "test123@example.com",
  "token": "eyJhbGc..."
}
```

**Save the token and user ID!**

---

**Test 4: Login**

```bash
curl -X POST https://your-app.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test123@example.com",
    "password": "Test123!"
  }'
```

Expected: Same response as registration with token

---

**Test 5: Get Questions (with auth)**

```bash
curl https://your-app.railway.app/api/questions/random?count=5&language=en \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

Expected: Array of 5 questions

---

**Test 6: Submit Exam**

```bash
curl -X POST https://your-app.railway.app/api/exams/submit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "userId": 1,
    "examType": "MOCK",
    "timeTakenSeconds": 60,
    "answers": [
      {
        "questionId": 1,
        "selectedAnswer": "A"
      }
    ]
  }'
```

Expected: Exam result with score

---

#### Step 3: **CRITICAL - Test Database Persistence**

This is the most important test to verify the database fix!

**A. Create a user**
```bash
curl -X POST https://your-app.railway.app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Persistence Test",
    "email": "persist@test.com",
    "password": "Test123!",
    "phoneNumber": "+971501234567"
  }'
```

**B. Force a redeployment**

Option 1: Make a small change and push
```bash
echo "# Test" >> README.md
git add README.md
git commit -m "Test deployment"
git push
```

Option 2: Manual redeploy in Railway
- Railway → Your Service → Click "Deploy" button

**C. Wait for deployment to complete (~2-3 minutes)**

**D. Try to login with the same user**
```bash
curl -X POST https://your-app.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "persist@test.com",
    "password": "Test123!"
  }'
```

**✅ SUCCESS:** If you get a token → Database persists! Problem solved!

**❌ FAILURE:** If you get "Invalid credentials" → Database not persisting

---

### Method 3: Check PostgreSQL Database Directly

**In Railway Dashboard:**

1. Click on **PostgreSQL** service (not your app)
2. Click **"Data"** tab
3. You should see tables:
   - `users`
   - `questions`
   - `exam_results`
   - `user_answers`

4. Click on `users` table
5. You should see the test users you created

**Via psql (Optional):**

1. Railway → PostgreSQL → Connect tab
2. Copy the connection command
3. Run in terminal:
   ```bash
   psql postgresql://postgres:password@host:5432/railway
   ```

4. List tables:
   ```sql
   \dt
   ```

5. Check users:
   ```sql
   SELECT id, full_name, email FROM users;
   ```

---

## Troubleshooting

### Issue: "Connection refused" in logs

**Problem:** PostgreSQL not connected

**Solution:**
1. Verify PostgreSQL is running (Railway → PostgreSQL → should be green)
2. Check environment variables (Railway → Your Service → Variables)
3. Should see: `DATABASE_URL`, `PGHOST`, `PGUSER`, etc.

---

### Issue: Still using H2 (logs show "H2Dialect")

**Problem:** `DATABASE_URL` not detected

**Solution:**
1. Check if PostgreSQL is added to project
2. Verify environment variables are injected
3. Manually set: `SPRING_PROFILES_ACTIVE=railway` in Railway variables

---

### Issue: Database data deleted after deployment

**Problem:** Not using PostgreSQL or volume not mounted

**Solution:**
1. Verify logs show "PostgreSQLDialect" not "H2Dialect"
2. If using H2, check volume is mounted at `/app/data`
3. Switch to PostgreSQL (recommended)

---

### Issue: Exam grading returns wrong results

**Problem:** Answer shuffling bug (already fixed!)

**Solution:**
1. Verify you pulled latest code
2. Check QuestionService doesn't shuffle answers
3. Check ExamService compares keys directly

---

## Quick Verification Command

Run all tests in one command:

```bash
# Replace with your Railway URL
export RAILWAY_URL="https://your-app.railway.app"

# Test health
curl $RAILWAY_URL/actuator/health

# Test topics
curl $RAILWAY_URL/api/questions/topics

# Test registration
curl -X POST $RAILWAY_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Quick Test","email":"quick@test.com","password":"Test123!","phoneNumber":"+971501234567"}'
```

---

## Success Criteria

Your deployment is ✅ **SUCCESSFUL** if:

1. ✓ Health endpoint returns `{"status":"UP"}`
2. ✓ Topics endpoint returns array of topics
3. ✓ Can register new user
4. ✓ Can login with registered user
5. ✓ Can get questions with auth token
6. ✓ Can submit exam and get results
7. ✓ **Users persist after redeployment** (MOST IMPORTANT!)
8. ✓ Logs show "PostgreSQLDialect" not "H2Dialect"
9. ✓ Exam grading returns correct scores

---

## Next Steps After Verification

Once everything works:

1. ✅ Test from frontend application
2. ✅ Monitor database size (Railway dashboard)
3. ✅ Set up error monitoring (optional)
4. ✅ Configure custom domain (optional)
5. ✅ Plan for database backups (Railway does auto-backups)

---

## Need Help?

If tests fail:

1. Check Railway logs first
2. Verify PostgreSQL connection
3. Run the automated script: `./verify-deployment.sh`
4. Share logs and I'll help debug!
