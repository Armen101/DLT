# Fix IntelliJ IDEA Dependencies

The error `package org.springframework.data.jpa.repository does not exist` means IntelliJ hasn't downloaded the Maven dependencies yet.

## Quick Fix (Recommended)

### Option 1: Using IntelliJ's Maven Tool Window

1. **Open Maven Tool Window**
   - Click on the **Maven** tab on the right side of IntelliJ
   - OR: View → Tool Windows → Maven

2. **Reload Maven Project**
   - Click the **Reload All Maven Projects** icon (circular arrows icon)
   - Wait for dependencies to download (this may take 2-5 minutes)

3. **Verify**
   - The red underlines should disappear
   - You should see "External Libraries" expanded in the Project view with Spring Boot libraries

### Option 2: Right-Click pom.xml

1. Right-click on `pom.xml` in the Project view
2. Select **Maven → Reload project**
3. Wait for dependencies to download

### Option 3: Using Menu

1. Go to **File → Reload All from Disk**
2. Then **File → Invalidate Caches → Just Restart**

### Option 4: Command Line (If Maven installed)

Close IntelliJ, then run:
```bash
cd /Users/armengevorgyan/IdeaProjects/DLT
mvn clean install
```

Then reopen IntelliJ - it will detect the downloaded dependencies.

## Detailed Steps

### Step 1: Verify Maven is Recognized

1. Open `pom.xml`
2. Look at the top-right corner - you should see a notification:
   - "Maven projects need to be imported"
   - Click **Import Changes** or **Enable Auto-Import**

### Step 2: Force Download Dependencies

In IntelliJ Terminal (bottom of screen):
```bash
mvn dependency:resolve
```

Or using the Maven tool window:
1. Open Maven tool window (right side)
2. Expand **DLT → Lifecycle**
3. Right-click **clean** → Run Maven Build
4. Then right-click **install** → Run Maven Build

### Step 3: Rebuild Project

After dependencies are downloaded:
1. **Build → Rebuild Project**
2. Wait for the build to complete

### Step 4: Configure Java SDK (if needed)

If you still see errors:

1. **File → Project Structure** (or Cmd+;)
2. **Project Settings → Project**
   - SDK: Select "zulu-21" (or your Java 17+ version)
   - Language Level: 17 or higher
3. **Project Settings → Modules**
   - Click on "DLT"
   - Sources tab: Make sure `src/main/java` is marked as Sources (blue folder)
   - Dependencies tab: Module SDK should be "Project SDK"
4. Click **OK**

## Verify Dependencies are Downloaded

After reloading, check:

1. **Project View → External Libraries**
   - You should see: Maven: org.springframework.boot:spring-boot-starter-web:3.2.0
   - And many other Spring libraries

2. **Maven Tool Window → Dependencies**
   - Should show all dependencies from pom.xml

## Common Issues

### Issue: "Cannot resolve symbol 'springframework'"

**Solution:**
1. Delete the `.idea` folder (close IntelliJ first)
2. Delete `target` folder
3. Reopen IntelliJ
4. When prompted, choose **Import Maven Project**

### Issue: "Maven not configured"

**Solution:**
1. **File → Settings** (or IntelliJ IDEA → Preferences on Mac)
2. **Build, Execution, Deployment → Build Tools → Maven**
3. Maven home directory: Should point to your Maven installation or use "Bundled (Maven 3)"
4. Click **OK**

### Issue: Downloads are very slow

**Solution:**
1. **File → Settings → Build, Execution, Deployment → Build Tools → Maven**
2. Add to **User settings file** (create if needed):

```xml
<settings>
  <mirrors>
    <mirror>
      <id>central</id>
      <mirrorOf>central</mirrorOf>
      <url>https://repo.maven.apache.org/maven2</url>
    </mirror>
  </mirrors>
</settings>
```

## After Dependencies are Downloaded

1. All red underlines should disappear
2. You can run the application:
   - Right-click `DubaiLicenseTheoryApplication.java`
   - Select **Run 'DubaiLicenseTheoryApplication'**

## Expected Download Size

Maven will download approximately **150-200 MB** of dependencies:
- Spring Boot framework
- Spring Data JPA
- Hibernate
- H2 Database
- Jackson JSON
- Validation libraries
- And their transitive dependencies

## Quick Checklist

- [ ] Open Maven tool window
- [ ] Click "Reload All Maven Projects" (circular arrows)
- [ ] Wait for "BUILD SUCCESS" message
- [ ] Verify "External Libraries" contains Spring Boot libraries
- [ ] Build → Rebuild Project
- [ ] Run the application

## Still Having Issues?

If dependencies still don't download:

1. Check your internet connection
2. Check if you're behind a corporate proxy (may need proxy settings in Maven)
3. Try deleting `~/.m2/repository` to force fresh download
4. Check IntelliJ logs: Help → Show Log in Finder

## Alternative: Use IntelliJ's Terminal

If nothing works, use IntelliJ's built-in terminal:

1. Open Terminal in IntelliJ (bottom)
2. Run: `mvn clean install -U`
3. The `-U` forces update of snapshots/releases
4. After successful build, restart IntelliJ
