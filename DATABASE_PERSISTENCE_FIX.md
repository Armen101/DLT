# Database Persistence Fix - Why Users Are Deleted After Deployment

## Problem

Your users are being deleted after each deployment because the H2 database file is stored **inside the Docker container** at `/app/data/dlt_db`. When Railway redeploys:

1. ❌ Old container destroyed → Database file **deleted**
2. ❌ New container starts → **Fresh empty** database created
3. ❌ All users, questions, exam results **gone**

## Solutions

### ✅ Solution 1: Use Railway Volume (H2 with Persistence) - QUICK FIX

**Already configured in your code!** Just need to set up in Railway dashboard:

#### Steps:

1. **Commit and push the updated `railway.json`:**
   ```bash
   git add railway.json
   git commit -m "Add volume mount for H2 database persistence"
   git push
   ```

2. **In Railway Dashboard:**
   - Go to your service → Settings
   - Scroll to "Volumes"
   - Click "Add Volume"
   - Set mount path: `/app/data`
   - Click "Add"
   - Redeploy your service

3. **That's it!** Your H2 database will now persist across deployments.

#### Pros:
- ✅ Quick setup (5 minutes)
- ✅ No code changes needed (already done)
- ✅ Works with current H2 setup

#### Cons:
- ⚠️ H2 not recommended for production (file-based, single connection)
- ⚠️ Volume backup requires manual export

---

### ✅ Solution 2: Use Railway PostgreSQL - RECOMMENDED FOR PRODUCTION

**Already configured in your code!** Just need to add PostgreSQL in Railway:

#### Steps:

1. **Add PostgreSQL to Railway Project:**
   - In Railway dashboard, click "+ New"
   - Select "Database" → "Add PostgreSQL"
   - Railway auto-generates: `DATABASE_URL`, `PGUSER`, `PGPASSWORD`, etc.

2. **Link PostgreSQL to your service:**
   - Railway automatically injects PostgreSQL environment variables
   - Your Dockerfile will detect `DATABASE_URL` and use PostgreSQL

3. **Commit and push changes:**
   ```bash
   git add pom.xml Dockerfile src/main/resources/application-railway.properties
   git commit -m "Add PostgreSQL support for Railway deployment"
   git push
   ```

4. **Deploy and verify:**
   - Railway will rebuild with PostgreSQL support
   - Database will persist across all deployments

#### Pros:
- ✅ Production-ready (managed by Railway)
- ✅ Automatic backups
- ✅ Better performance
- ✅ No manual volume management
- ✅ Scales better

#### Cons:
- 💰 PostgreSQL costs $5/month on Railway (after free trial)

---

## What Was Changed in Your Code

### 1. `railway.json` (Volume Support)
```json
"volumeMounts": [
  {
    "mountPath": "/app/data",
    "name": "h2-database"
  }
]
```

### 2. `pom.xml` (PostgreSQL Dependency)
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 3. `application-railway.properties` (PostgreSQL Config)
```properties
spring.datasource.url=${DATABASE_URL}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### 4. `Dockerfile` (Auto-detect Database)
```bash
# Uses PostgreSQL if DATABASE_URL exists, otherwise H2
if [ -n "$DATABASE_URL" ]; then
  java -jar app.jar --spring.profiles.active=railway
else
  java -jar app.jar --spring.profiles.active=prod
fi
```

---

## Recommendation

**For MVP/Testing:** Use Solution 1 (H2 + Volume) - Quick and free

**For Production:** Use Solution 2 (PostgreSQL) - Reliable and scalable

---

## Verification

After deploying either solution:

1. Create a test user
2. Redeploy your app (push a small change)
3. Check if the user still exists

✅ If user persists → Database persistence working!
❌ If user gone → Volume not mounted or PostgreSQL not connected

---

## Need Help?

- Railway Volumes: https://docs.railway.app/reference/volumes
- Railway PostgreSQL: https://docs.railway.app/databases/postgresql
