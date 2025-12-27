# Railway PostgreSQL Setup Guide

## ✅ Code Changes Complete!

All code changes are committed and ready. Now follow these steps to set up PostgreSQL in Railway.

---

## Step-by-Step Setup

### 1. Push Changes to Repository

```bash
git push origin master
```

This will push all the PostgreSQL configuration to your repository.

---

### 2. Add PostgreSQL Database in Railway

#### Option A: From Railway Dashboard (Recommended)

1. **Open your Railway project**: https://railway.app/project/[your-project-id]

2. **Add PostgreSQL database**:
   - Click the **"+ New"** button (top right)
   - Select **"Database"**
   - Choose **"Add PostgreSQL"**
   - Railway will automatically create the database

3. **Wait for deployment**:
   - PostgreSQL will provision (takes ~30 seconds)
   - You'll see it appear in your project with a purple icon

#### Option B: Using Railway CLI

```bash
# Install Railway CLI if you haven't
npm i -g @railway/cli

# Login
railway login

# Link to your project
railway link

# Add PostgreSQL
railway add --database postgresql
```

---

### 3. Verify PostgreSQL Environment Variables

Railway **automatically injects** these environment variables into your service:

- `DATABASE_URL` - Full PostgreSQL connection string
- `PGHOST` - PostgreSQL host
- `PGPORT` - PostgreSQL port (5432)
- `PGUSER` - Database username
- `PGPASSWORD` - Database password
- `PGDATABASE` - Database name

**To verify:**

1. Go to your **service** (not the PostgreSQL database)
2. Click **"Variables"** tab
3. You should see `DATABASE_URL` and other `PG*` variables listed
4. If not visible, they're still injected at runtime (Railway does this automatically)

---

### 4. Redeploy Your Application

Railway will **automatically redeploy** when you push changes, but you can also trigger manually:

#### Option A: Automatic (Recommended)
- Push to GitHub → Railway auto-deploys
- Already done in Step 1!

#### Option B: Manual Trigger
1. Go to your service in Railway
2. Click **"Deployments"** tab
3. Click **"Deploy"** button

---

### 5. Monitor Deployment Logs

1. In Railway, go to your service
2. Click on the latest deployment
3. Watch the **"Deploy Logs"**

**Look for these success indicators:**

```
✓ Building with Dockerfile
✓ Copying jar file
✓ Starting application with railway profile
✓ HikariCP - Starting connection pool
✓ Using PostgreSQL dialect
✓ Started DubaiLicenseTheoryApplication
```

**❌ If you see errors like:**
```
Unable to obtain connection from database
```
→ PostgreSQL not connected. Check Step 3.

---

### 6. Verify Database Connection

#### Test 1: Check Application Health

```bash
curl https://your-app.railway.app/api/auth/health
```

Should return `200 OK`

#### Test 2: Create a Test User

```bash
curl -X POST https://your-app.railway.app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test@example.com",
    "password": "Test123!",
    "phoneNumber": "+971501234567"
  }'
```

#### Test 3: Verify Persistence (THE IMPORTANT ONE!)

1. **Create a user** (using Test 2 above)
2. **Trigger a redeployment**:
   - Make a small change (add a comment to README)
   - Push to GitHub
   - Wait for Railway to redeploy
3. **Try to login with the same user**:
   ```bash
   curl -X POST https://your-app.railway.app/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "email": "test@example.com",
       "password": "Test123!"
     }'
   ```

4. **✅ SUCCESS**: If login works → Database persisted!
5. **❌ FAILURE**: If user not found → Database not persisting (contact me)

---

### 7. Access PostgreSQL Database (Optional)

#### View Data in Railway Dashboard

1. Click on the **PostgreSQL** service in Railway
2. Click **"Data"** tab
3. Browse tables: `users`, `questions`, `exam_results`, etc.

#### Connect via psql

Railway provides a connection command:

1. Click PostgreSQL service
2. Click **"Connect"** tab
3. Copy the `psql` command
4. Run in your terminal

```bash
psql postgresql://postgres:password@host:5432/railway
```

#### Connect via GUI (TablePlus, DBeaver, pgAdmin)

Use the connection details from Railway:
- **Host**: `PGHOST` value
- **Port**: `5432`
- **Database**: `PGDATABASE` value
- **Username**: `PGUSER` value
- **Password**: `PGPASSWORD` value

---

## Troubleshooting

### Issue: "No suitable driver found for jdbc:postgresql"

**Solution**: PostgreSQL dependency not included

```bash
# Verify pom.xml has PostgreSQL
grep -A3 "postgresql" pom.xml
```

Should show:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
```

### Issue: "DATABASE_URL environment variable not set"

**Solution**: PostgreSQL not linked to service

1. Go to Railway project
2. Click PostgreSQL database
3. Click **"Connect"** tab
4. Ensure your service is listed under "Connected Services"
5. If not, click **"Add Service"** and select your app

### Issue: Still using H2 after adding PostgreSQL

**Check logs** for this line:
```
Using H2Dialect
```

**Solution**: `DATABASE_URL` not detected

1. Verify env vars in Railway (Step 3)
2. Check Dockerfile ENTRYPOINT (should auto-detect)
3. Manually set profile:
   - Railway → Your Service → Variables
   - Add: `SPRING_PROFILES_ACTIVE=railway`

### Issue: Application crashes on startup

**Check logs** for error details:

```bash
# In Railway, click deployment → View Logs
```

Common errors:
- `Connection refused` → PostgreSQL not running
- `Invalid credentials` → Wrong `PGUSER`/`PGPASSWORD`
- `Database does not exist` → Use Railway-provided `PGDATABASE`

---

## Cost Information

**PostgreSQL on Railway:**
- **Free Tier**: $5 credit/month (usually enough for development)
- **Paid**: $5/month for PostgreSQL addon after free credits
- **Alternative**: Use free H2 with volume mount (see DATABASE_PERSISTENCE_FIX.md)

---

## Migration from H2 to PostgreSQL

If you already have data in H2 that you want to migrate:

### Export from H2

```bash
# Connect to H2 console (local)
# Download your data as SQL or CSV

# Or use this approach:
curl http://localhost:8080/h2-console
```

### Import to PostgreSQL

```bash
# Connect to Railway PostgreSQL
psql $DATABASE_URL < backup.sql
```

**Note**: You'll need to convert H2-specific SQL to PostgreSQL syntax if needed.

---

## Next Steps After Setup

1. ✅ Verify users persist across deployments
2. ✅ Test all API endpoints
3. ✅ Set up database backups (Railway does this automatically)
4. ✅ Monitor database size (Railway dashboard shows usage)
5. ✅ Consider adding database indices for performance (later)

---

## Summary Checklist

- [ ] Push code to GitHub
- [ ] Add PostgreSQL in Railway dashboard
- [ ] Verify `DATABASE_URL` environment variable
- [ ] Watch deployment logs for PostgreSQL connection
- [ ] Create test user
- [ ] Redeploy and verify user still exists
- [ ] Celebrate! 🎉 Database now persists!

---

## Need Help?

If something doesn't work:

1. Check Railway deployment logs
2. Verify environment variables
3. Test database connection
4. Review troubleshooting section above

**Still stuck?** Share the deployment logs and I'll help debug!
