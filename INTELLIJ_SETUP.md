# IntelliJ IDEA Setup Guide

## The Problem

You're seeing errors like:
```
java: package org.springframework.data.jpa.repository does not exist
```

This means **IntelliJ hasn't downloaded the Maven dependencies yet**.

## The Solution (Choose One)

### ✅ EASIEST: Use IntelliJ's Maven Tool Window

**Step-by-step with screenshots locations:**

1. **Open Maven Tool Window**
   - Look at the **right side** of IntelliJ window
   - Click on the **Maven** tab (vertical text)
   - If you don't see it: Menu → View → Tool Windows → Maven

2. **Reload Maven Project**
   - In the Maven tool window, look at the **top toolbar**
   - Click the **"Reload All Maven Projects"** icon
   - It looks like **circular arrows** (↻)
   - **Location**: Top-left corner of Maven tool window

3. **Wait for Download**
   - Bottom of IntelliJ shows progress: "Resolving dependencies..."
   - This takes **2-5 minutes** (downloading ~200 MB)
   - You'll see: "BUILD SUCCESS"

4. **Rebuild Project**
   - Menu → Build → Rebuild Project
   - Wait for it to finish

5. **Done!**
   - Red underlines should disappear
   - All Spring imports should now work

---

### Alternative 1: Right-Click pom.xml

1. In Project view (left side), find **pom.xml**
2. **Right-click** on pom.xml
3. Select **Maven → Reload project**
4. Wait for download to complete

---

### Alternative 2: Auto-Import Prompt

When you open pom.xml, you might see a notification at the top:

```
"Maven projects need to be imported"
```

**Click**: "Enable Auto-Import" or "Import Changes"

---

### Alternative 3: Command Line (If you have Maven)

**Option A: In IntelliJ Terminal**
1. Open Terminal in IntelliJ (bottom toolbar)
2. Run:
   ```bash
   mvn dependency:resolve
   ```

**Option B: Use our script**
1. Open Terminal in IntelliJ
2. Run:
   ```bash
   ./download-dependencies.sh
   ```

---

## How to Verify It Worked

### Check 1: External Libraries

1. In Project view (left side)
2. Expand **External Libraries**
3. You should see many entries like:
   - Maven: org.springframework.boot:spring-boot-starter-web:3.2.0
   - Maven: org.springframework.data:spring-data-jpa:3.2.0
   - Maven: org.hibernate:hibernate-core:...
   - And many more

### Check 2: Maven Dependencies View

1. Open Maven tool window (right side)
2. Expand: **DLT → Dependencies**
3. You should see all dependencies listed

### Check 3: No Red Underlines

1. Open any Java file (e.g., `UserController.java`)
2. The `import` statements should **NOT** be red
3. Example: `import org.springframework.web.bind.annotation.*;` should be recognized

---

## Still Not Working?

### Problem: Maven Tool Window Not Visible

**Solution:**
1. View → Tool Windows → Maven
2. If still not there: File → Project Structure → Modules → Import Module → Select pom.xml

### Problem: Downloads Are Stuck

**Solution:**
1. Close IntelliJ
2. Delete folder: `/Users/armengevorgyan/.m2/repository`
3. Reopen IntelliJ
4. Reload Maven project again (fresh download)

### Problem: "Cannot resolve symbol 'springframework'"

**Solution:**
1. **File → Invalidate Caches → Invalidate and Restart**
2. After restart, reload Maven project

### Problem: Maven Shown But Not Downloading

**Solution:**
1. File → Settings (or Preferences on Mac)
2. Build, Execution, Deployment → Build Tools → Maven
3. Check "Maven home directory": Should say "Bundled (Maven 3)"
4. Click Apply → OK
5. Reload Maven project

---

## Complete Reset (Nuclear Option)

If nothing works:

1. **Close IntelliJ**
2. **Delete these folders:**
   ```bash
   rm -rf /Users/armengevorgyan/IdeaProjects/DLT/.idea
   rm -rf /Users/armengevorgyan/IdeaProjects/DLT/target
   rm -rf /Users/armengevorgyan/IdeaProjects/DLT/out
   ```
3. **Reopen IntelliJ**
4. **Open the project**: File → Open → Select DLT folder
5. **When prompted**: "Import Maven Project" → Click Yes
6. **Wait for indexing and dependency download**

---

## What Gets Downloaded?

Maven will download these Spring Boot libraries:
- ✓ Spring Boot Web (REST APIs)
- ✓ Spring Data JPA (Database access)
- ✓ Hibernate (ORM)
- ✓ H2 Database
- ✓ Validation Framework
- ✓ Jackson (JSON)
- ✓ Lombok (Code generation)
- ✓ All their dependencies (~150-200 MB total)

---

## Quick Checklist

Before asking for help, verify:

- [ ] I opened the Maven tool window
- [ ] I clicked "Reload All Maven Projects" (circular arrows icon)
- [ ] I waited at least 5 minutes for download
- [ ] I see "BUILD SUCCESS" message
- [ ] I see libraries in "External Libraries"
- [ ] I tried "Build → Rebuild Project"
- [ ] I checked my internet connection

---

## After Dependencies Are Downloaded

You can run the application:

1. Navigate to: `src/main/java/com/dubai/dlt/DubaiLicenseTheoryApplication.java`
2. **Right-click** on the file
3. Select **"Run 'DubaiLicenseTheoryApplication'"**
4. Server starts on: http://localhost:8080

---

## Need More Help?

1. Check IntelliJ's Event Log (bottom-right corner)
2. Look for Maven-related errors
3. Check console output for error messages

**Common Error Messages and Solutions:**

| Error | Solution |
|-------|----------|
| "Plugin not found" | Reload Maven project |
| "Cannot download" | Check internet connection |
| "Unknown host" | Check firewall/proxy settings |
| "Build failure" | Check Java version (need 17+) |
