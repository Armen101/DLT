# Complete IntelliJ Setup Guide - Step by Step

Follow these steps in order to get your project working.

## Current Status

You're seeing: `Maven resources compiler: Maven project configuration required...`

This means IntelliJ needs to download Maven dependencies and configure the build properly.

---

## 🎯 Complete Fix - Follow These Steps

### Step 1: Open Maven Settings

1. Open **Preferences/Settings**:
   - **Mac**: IntelliJ IDEA menu → Preferences (or press Cmd+,)
   - **Windows/Linux**: File → Settings (or press Ctrl+Alt+S)

### Step 2: Configure Maven

2. In the Settings window, navigate to:
   ```
   Build, Execution, Deployment → Build Tools → Maven
   ```

3. On the **Maven** main page:
   - **Maven home directory**: Select "Bundled (Maven 3.x)" from dropdown
   - Leave other settings as default

4. Click on **Importing** (under Maven in the left sidebar):
   - ✅ Check "Import Maven projects automatically"
   - ✅ Check "Automatically download: Sources"
   - ✅ Check "Automatically download: Documentation"

5. Click on **Runner** (under Maven in the left sidebar):
   - ✅ Check "Delegate IDE build/run actions to Maven"

6. Click **Apply**, then click **OK**

### Step 3: Reload Maven Project

7. **Find your `pom.xml` file**:
   - It's in the root of your project (in Project view on the left)

8. **Right-click** on `pom.xml`

9. Select **Maven → Reload project**

10. **Wait for dependencies to download**:
    - Look at the bottom status bar
    - You'll see: "Resolving dependencies..." or "Downloading..."
    - This takes **2-5 minutes** (downloading ~200 MB)
    - Wait until you see "BUILD SUCCESS"

### Step 4: Verify Maven Tool Window

11. Look at the **right edge** of IntelliJ window:
    - You should see a **Maven** tab (vertical text)
    - Click on it to open the Maven tool window

12. In the Maven tool window, you should see:
    ```
    driver-license-theory
    ├── Lifecycle
    │   ├── clean
    │   ├── validate
    │   ├── compile
    │   └── ...
    ├── Plugins
    └── Dependencies
        └── (list of all downloaded dependencies)
    ```

### Step 5: Rebuild the Project

13. Go to menu: **Build → Rebuild Project**

14. Wait for the build to complete

15. **Check for errors**:
    - Bottom panel shows build messages
    - Should see: "Build completed successfully"
    - NO red errors about missing packages

### Step 6: Verify External Libraries

16. In **Project view** (left side), expand **External Libraries**

17. You should see many Maven libraries:
    - Maven: org.springframework.boot:spring-boot-starter-web:3.2.0
    - Maven: org.springframework.data:spring-data-jpa:3.2.0
    - Maven: org.hibernate:hibernate-core:...
    - And many more (50+ libraries)

### Step 7: Run the Application

18. Navigate to: `src/main/java/com/dubai/dlt/DubaiLicenseTheoryApplication.java`

19. **Right-click** on the file

20. Select **"Run 'DubaiLicenseTheoryApplication'"**

21. The application should start!
    - Console shows Spring Boot ASCII art logo
    - Last line says: "Started DubaiLicenseTheoryApplication in X.XX seconds"
    - Server running on: http://localhost:8080

---

## ✅ Success Checklist

After following all steps, verify:

- [ ] Maven tool window is visible (right side)
- [ ] Dependencies are downloaded (check External Libraries)
- [ ] No red underlines in Java files
- [ ] Build succeeds without errors
- [ ] Application runs and starts on port 8080

---

## 🚨 Troubleshooting

### Problem: Maven Tool Window Doesn't Appear

**Solution:**
1. Menu: **View → Tool Windows → Maven**
2. If it says "No Maven projects found":
   - Click the **+** button in Maven window
   - Navigate to and select your `pom.xml`
   - Click OK

### Problem: Dependencies Not Downloading

**Solution:**
1. In Maven tool window, click **↻ (Reload All Maven Projects)** icon
2. Check internet connection
3. Wait longer (first download can take 5+ minutes)
4. Check console for error messages

### Problem: Still Getting Build Errors

**Solution:**
1. **File → Invalidate Caches → Invalidate and Restart**
2. After restart:
   - Right-click `pom.xml` → Maven → Reload project
   - Build → Rebuild Project

### Problem: "Cannot resolve symbol 'springframework'"

**Solution:**
This means dependencies aren't downloaded yet:
1. Wait for Maven to finish downloading (check bottom status bar)
2. Make sure External Libraries shows Spring libraries
3. Try Build → Rebuild Project

---

## 📋 Quick Reference

### Where is Everything?

| Item | Location |
|------|----------|
| Maven Settings | Preferences → Build, Execution, Deployment → Build Tools → Maven |
| Maven Tool Window | Right edge of window, or View → Tool Windows → Maven |
| Reload Maven | Right-click pom.xml → Maven → Reload project |
| Rebuild Project | Menu: Build → Rebuild Project |
| Run Application | Right-click DubaiLicenseTheoryApplication.java → Run |
| External Libraries | Project view (left) → Expand "External Libraries" |

### Important Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven configuration (dependencies list) |
| `DubaiLicenseTheoryApplication.java` | Main application (run this) |
| `src/main/resources/questions.json` | Your 1500 questions (add this!) |
| `src/main/resources/application.properties` | App settings |

---

## 🎬 After Setup is Complete

### 1. Add Your Questions

Copy your 1500 questions JSON file to:
```
src/main/resources/questions.json
```

### 2. Run the Application

Run `DubaiLicenseTheoryApplication.java` as described above.

### 3. Test the API

Open browser or terminal:

```bash
# Get all topics
curl http://localhost:8080/api/questions/topics

# Get 5 random questions in Arabic
curl "http://localhost:8080/api/questions/random?count=5&language=ar"
```

### 4. Connect Your Mobile App

Configure your mobile app to use:
```
Base URL: http://localhost:8080/api
```

See [API_EXAMPLES.md](API_EXAMPLES.md) for all available endpoints.

---

## ⏱️ Time Expectations

- **Maven Configuration**: 2 minutes
- **Dependency Download**: 2-5 minutes (first time only)
- **Project Build**: 1-2 minutes
- **Application Startup**: 5-10 seconds

**Total**: About 10 minutes for first-time setup

---

## 💡 Pro Tips

1. **Enable Auto-Import**: In Maven settings → Importing → Check "Import Maven projects automatically"
2. **Keep Maven Window Open**: Useful for monitoring build progress
3. **Check Console**: Always check console output for error details
4. **Use Terminal in IntelliJ**: Built-in terminal is at the bottom

---

## 📞 Still Need Help?

If you're still stuck after following all steps:

1. Check the error message in the console
2. Look at the Event Log (bottom-right corner of IntelliJ)
3. Verify your Java version: `java -version` (should be 17 or higher)
4. Check the detailed guides:
   - [FIX_BUILD_ERROR.md](FIX_BUILD_ERROR.md)
   - [ENABLE_MAVEN.md](ENABLE_MAVEN.md)
   - [FIX_DEPENDENCIES.md](FIX_DEPENDENCIES.md)

---

## Summary: What You Need to Do

1. ✅ Open Settings → Maven → Configure as shown above
2. ✅ Right-click pom.xml → Maven → Reload project
3. ✅ Wait for dependencies to download
4. ✅ Build → Rebuild Project
5. ✅ Run DubaiLicenseTheoryApplication.java
6. ✅ Add your questions.json file
7. ✅ Test the API endpoints

**That's it!** Your backend server will be ready for your mobile app.
