# Test Database Persistence - The Real Issue

## The Problem

After deployment:
1. You create a user
2. You redeploy the app
3. You try to login with that user → **401 Unauthorized**

This means: **The database is still being wiped on each deployment!**

---

## Root Cause Analysis

The database is being deleted because **you're still using H2** (file-based database inside the container), not PostgreSQL.

---

## Step-by-Step Verification

### Step 1: Check What Database You're Actually Using

**In Railway Dashboard:**

1. Go to your project
2. Click on your **application service** (not PostgreSQL)
3. Click **"Deployments"** → Latest deployment → **"View Logs"**

**Search for this line in logs:**

```
Using dialect: org.hibernate.dialect.H2Dialect
```

**❌ If you see `H2Dialect`:** You're using H2 (database gets deleted on redeploy)

**✅ If you see `PostgreSQLDialect`:** You're using PostgreSQL (database persists)

---

### Step 2: Check If PostgreSQL Is Added to Your Project

**In Railway Dashboard:**

1. Go to your project
2. Look for a **PostgreSQL service** (purple icon with elephant)

**❌ If you DON'T see PostgreSQL:** You haven't added it yet (this is the issue!)

**✅ If you DO see PostgreSQL:** It's added but might not be connected

---

### Step 3: Check Environment Variables

**In Railway Dashboard:**

1. Click on your **application service**
2. Click **"Variables"** tab
3. Look for these variables:
   - `DATABASE_URL`
   - `PGHOST`
   - `PGUSER`
   - `PGPASSWORD`

**❌ If these variables are MISSING:** PostgreSQL is not connected to your app

**✅ If these variables exist:** PostgreSQL is connected, but check the logs

---

## Quick Diagnosis Commands

Run these in order:

### Test 1: What database is running?

```bash
# Check Railway logs
# Look for "Using dialect"

# If logs show H2Dialect → Still using H2 (not persisting!)
# If logs show PostgreSQLDialect → Using PostgreSQL (should persist)
```

---

## The Fix (Step-by-Step)

### Option 1: You Haven't Added PostgreSQL Yet

**Do this:**

1. **In Railway Dashboard:**
   - Click **"+ New"**
   - Select **"Database"**
   - Click **"Add PostgreSQL"**
   - Wait 30 seconds for it to provision

2. **Verify connection:**
   - Railway should automatically inject `DATABASE_URL` into your app
   - Check: Your App → Variables → Should see `DATABASE_URL`

3. **Redeploy:**
   - Your app will auto-redeploy
   - OR push a small change: `git commit --allow-empty -m "trigger redeploy" && git push`

4. **Check logs:**
   - Should now show: `Using dialect: PostgreSQLDialect`

---

### Option 2: PostgreSQL Added But Not Connected

**Do this:**

1. **In Railway Dashboard:**
   - Click on **PostgreSQL** service
   - Click **"Connect"** tab
   - Under "Connected Services", check if your app is listed

2. **If NOT listed:**
   - Click **"Add Service"**
   - Select your application
   - This will inject the environment variables

3. **Redeploy** and check logs again

---

### Option 3: Using H2 with Volume (Alternative)

If you don't want to pay for PostgreSQL:

1. **Verify volume is mounted in Railway:**
   - Railway → Your Service → **"Settings"**
   - Scroll to **"Volumes"**
   - Should see a volume mounted at `/app/data`

2. **If NO volume:**
   - Click **"Add Volume"**
   - Mount path: `/app/data`
   - Click **"Add"**

3. **Redeploy**

**Note:** H2 with volume is less reliable than PostgreSQL!

---

## Simple Persistence Test

After fixing the database connection:

### 1. Create a user

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

Expected: Success with token

---

### 2. Trigger a redeploy

**Option A: Empty commit**
```bash
git commit --allow-empty -m "Test persistence"
git push
```

**Option B: Railway dashboard**
- Your Service → Click "Deploy" button

---

### 3. Wait for deployment to complete

Watch the logs:
- Railway → Your Service → Deployments → Latest

Wait until you see:
```
Started DubaiLicenseTheoryApplication in X seconds
```

---

### 4. Try to login with the SAME user

```bash
curl -X POST https://your-app.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "persist@test.com",
    "password": "Test123!"
  }'
```

**✅ SUCCESS:** Returns token → Database persists!

**❌ FAILURE:** Returns 401 → Database still being deleted!

---

## What You Should See

### ✅ Correct Setup (PostgreSQL Working)

**In Railway Logs:**
```
HikariCP - Starting connection pool
Using dialect: org.hibernate.dialect.PostgreSQLDialect
Initialized JPA EntityManagerFactory
Started DubaiLicenseTheoryApplication in 12.345 seconds
```

**In Railway Variables:**
```
DATABASE_URL=postgresql://postgres:xxx@xxx.railway.app:5432/railway
PGHOST=xxx.railway.app
PGUSER=postgres
PGPASSWORD=xxx
PGDATABASE=railway
```

**Test Result:**
- Create user → Success
- Redeploy → Success
- Login with same user → **Success** (returns token)

---

### ❌ Wrong Setup (Still Using H2)

**In Railway Logs:**
```
Using dialect: org.hibernate.dialect.H2Dialect
```

**In Railway Variables:**
```
(No DATABASE_URL variable)
```

**Test Result:**
- Create user → Success
- Redeploy → Success
- Login with same user → **401 Unauthorized** (user gone!)

---

## Action Items for You

**Right now, do these steps in order:**

1. **Check Railway logs** - What dialect is being used?
   - Go to: Railway → Your Service → Deployments → Latest → View Logs
   - Search for "dialect"
   - Tell me: H2Dialect or PostgreSQLDialect?

2. **Check if PostgreSQL exists** in your Railway project
   - Go to: Railway → Your Project
   - Do you see a purple PostgreSQL icon?
   - Tell me: Yes or No?

3. **Check environment variables**
   - Go to: Railway → Your Service → Variables
   - Do you see `DATABASE_URL`?
   - Tell me: Yes or No?

**Based on your answers, I'll tell you exactly what to do next.**

---

## Why This Matters

**H2 (file in container):**
```
Deploy → Container with /app/data/db file
Redeploy → NEW container → No /app/data/db → Fresh database → Users gone!
```

**PostgreSQL (external database):**
```
Deploy → Container connects to PostgreSQL server
Redeploy → NEW container connects to SAME PostgreSQL server → Users still there!
```

---

## Summary

The issue is NOT with the API or authentication. The issue is:

**Your database is inside the container, and the container is destroyed on each deployment.**

**Solution:** Use PostgreSQL (external database that survives redeployments)

Tell me the answers to the 3 questions above, and I'll guide you through the exact fix!
