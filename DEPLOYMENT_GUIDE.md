# Railway Deployment Guide for drivertheoryuae.com

## Prerequisites
- Railway account with $5 package
- GitHub repository (recommended) or Railway CLI
- Squarespace domain: drivertheoryuae.com

## Step 1: Deploy to Railway

### Option A: Deploy via GitHub (Recommended)

1. **Push your code to GitHub:**
   ```bash
   git add .
   git commit -m "Add Railway deployment configuration"
   git push origin master
   ```

2. **Connect to Railway:**
   - Go to https://railway.app
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Select your repository
   - Railway will automatically detect the Dockerfile and deploy

### Option B: Deploy via Railway CLI

1. **Install Railway CLI:**
   ```bash
   npm install -g @railway/cli
   ```

2. **Login to Railway:**
   ```bash
   railway login
   ```

3. **Initialize and deploy:**
   ```bash
   railway init
   railway up
   ```

## Step 2: Configure Environment Variables in Railway

After deployment, add these environment variables in Railway dashboard:

1. Go to your project in Railway
2. Click on your service
3. Go to "Variables" tab
4. Add the following:

```
SPRING_PROFILES_ACTIVE=prod
PORT=8080
```

Optional (if you want to add MySQL database instead of H2):
```
DATABASE_URL=<railway-mysql-url>
DB_USERNAME=<username>
DB_PASSWORD=<password>
```

## Step 3: Get Your Railway URL

After deployment:
1. Go to your service in Railway dashboard
2. Click "Settings" tab
3. Under "Domains", you'll see your Railway-provided URL (e.g., `your-app.up.railway.app`)
4. Test your API: `https://your-app.up.railway.app/api/health`

## Step 4: Connect Custom Domain from Squarespace

### Part A: In Railway Dashboard

1. Go to your service in Railway
2. Click "Settings" tab
3. Under "Domains" section, click "Custom Domain"
4. Enter: `drivertheoryuae.com`
5. Railway will show you DNS records to add

You should see something like:
- **Type**: CNAME
- **Name**: @ (or www)
- **Value**: your-app.up.railway.app

### Part B: In Squarespace

1. **Login to Squarespace:**
   - Go to https://account.squarespace.com
   - Navigate to your domain settings for drivertheoryuae.com

2. **Access DNS Settings:**
   - Click on the domain name
   - Go to "DNS Settings" or "Advanced DNS"

3. **Add DNS Records:**

   For root domain (drivertheoryuae.com):
   - **Type**: A Record
   - **Host**: @
   - **Points To**: (Railway will provide an IP, or use CNAME flattening if available)

   For www subdomain (www.drivertheoryuae.com):
   - **Type**: CNAME
   - **Host**: www
   - **Points To**: your-app.up.railway.app

   **Note**: Some DNS providers don't allow CNAME for root domain. In that case:
   - Use A record with the IP address provided by Railway
   - Or use ALIAS/ANAME record if Squarespace supports it

4. **Save DNS Changes**

### Part C: Alternative - Point to Railway's IP

If Squarespace doesn't support CNAME for root domain:

1. In Railway, go to Settings > Networking
2. Get the static IP address (if available in your plan)
3. In Squarespace DNS:
   - **Type**: A
   - **Host**: @
   - **Value**: <Railway IP address>

## Step 5: Wait for DNS Propagation

- DNS changes can take 1-48 hours (usually 15-30 minutes)
- Check status: `nslookup drivertheoryuae.com`
- Test: `curl https://drivertheoryuae.com/api/health`

## Step 6: Enable HTTPS

Railway automatically provides SSL certificates for custom domains:
1. After DNS is configured, Railway will automatically provision SSL
2. Your site will be accessible via `https://drivertheoryuae.com`

## Step 7: Verify Deployment

Test these endpoints:
```bash
# Health check
curl https://drivertheoryuae.com/api/health

# Get categories
curl https://drivertheoryuae.com/api/categories

# Register user
curl -X POST https://drivertheoryuae.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test123","email":"test@example.com"}'
```

## Troubleshooting

### Build Fails
- Check Railway logs in the dashboard
- Ensure Java 17 is specified in Dockerfile
- Verify pom.xml dependencies

### App Crashes
- Check "Deployments" tab in Railway for logs
- Verify environment variables are set
- Check if port 8080 is exposed

### Domain Not Working
- Verify DNS records are correct
- Wait for DNS propagation (up to 48 hours)
- Check Railway domain status in Settings
- Use `dig drivertheoryuae.com` to check DNS

### Database Issues
- For H2: Railway provides persistent volumes
- For MySQL: Add Railway MySQL plugin and update environment variables

## Cost Optimization

With the $5 Railway plan:
- Monitor usage in Railway dashboard
- H2 database is included (uses disk storage)
- Consider upgrading to MySQL if you need better performance

## Updating Your Application

```bash
# Make changes to your code
git add .
git commit -m "Update application"
git push origin master

# Railway will automatically redeploy
```

## Monitoring

1. **Railway Dashboard:**
   - View logs in real-time
   - Monitor CPU/Memory usage
   - Check deployment status

2. **Application Logs:**
   - Click on your service
   - Go to "Deployments" tab
   - Click on latest deployment to see logs

## Support Resources

- Railway Docs: https://docs.railway.app
- Railway Discord: https://discord.gg/railway
- Squarespace Support: https://support.squarespace.com
