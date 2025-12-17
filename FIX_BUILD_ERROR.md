# Fix: Maven Resources Compiler Error

## Error Message
```
Maven resources compiler: Maven project configuration required for module 'driver-license-theory'
isn't available. Compilation of Maven projects is supported only if external build is started from an IDE.
```

## What This Means
IntelliJ is trying to compile the project but the Maven configuration isn't fully loaded yet.

## Solution: Change Build Settings

### Method 1: Delegate Build to Maven (RECOMMENDED)

1. **Open Settings**
   - Mac: **IntelliJ IDEA → Preferences** (or Cmd+,)
   - Windows/Linux: **File → Settings** (or Ctrl+Alt+S)

2. **Navigate to Build Settings**
   - Go to: **Build, Execution, Deployment → Build Tools → Maven → Runner**

3. **Enable Delegation**
   - Check: ✅ **"Delegate IDE build/run actions to Maven"**
   - Click **Apply**

4. **Also check Importing settings**
   - Go to: **Build, Execution, Deployment → Build Tools → Maven → Importing**
   - Check: ✅ **"Import Maven projects automatically"**
   - Click **Apply → OK**

5. **Reload Maven Project**
   - Right-click `pom.xml` → **Maven → Reload project**
   - OR: Maven tool window → Click Reload icon (↻)

### Method 2: Reimport Maven Project

1. **File → Invalidate Caches → Invalidate and Restart**
2. After restart:
   - Right-click on `pom.xml`
   - Select **Maven → Reimport**
3. Wait for the import to complete

### Method 3: Fix Project Structure

1. **File → Project Structure** (Cmd+; or Ctrl+Alt+Shift+S)

2. **Project Settings → Project**
   - SDK: Select your Java SDK (zulu-21)
   - Language level: 17 or higher

3. **Project Settings → Modules**
   - Select "driver-license-theory"
   - **Sources tab:**
     - `src/main/java` should be marked as **Sources** (blue folder)
     - `src/main/resources` should be marked as **Resources** (purple folder)
     - `src/test/java` should be marked as **Tests** (green folder)
     - `target` should be **Excluded** (red folder)

4. **Click OK**

5. **Rebuild Project**
   - Build → Rebuild Project

## Alternative: Use IntelliJ's Build System

If Maven delegation doesn't work:

1. **Settings → Build, Execution, Deployment → Build Tools → Maven → Runner**
2. **UNCHECK** "Delegate IDE build/run actions to Maven"
3. Let IntelliJ use its own build system

Then:
1. **Build → Rebuild Project**
2. Wait for dependencies to download first

## Step-by-Step Complete Fix

### Step 1: Ensure Maven is Loaded

1. Look for **Maven tool window** on the right side
2. If you see it, click the **↻ Reload All Maven Projects** icon
3. Wait for "BUILD SUCCESS" message
4. This downloads all dependencies (~200 MB, takes 2-5 minutes)

### Step 2: Configure Build Settings

1. **Preferences/Settings** (Cmd+, or Ctrl+Alt+S)
2. **Build, Execution, Deployment → Build Tools → Maven**
3. **Importing tab:**
   - ✅ Import Maven projects automatically
   - ✅ Automatically download: Sources
   - ✅ Automatically download: Documentation
4. **Runner tab:**
   - ✅ Delegate IDE build/run actions to Maven
5. Click **Apply → OK**

### Step 3: Reload and Rebuild

1. Right-click `pom.xml` → **Maven → Reload project**
2. Wait for reload to complete
3. **Build → Rebuild Project**
4. Errors should be gone

## Quick Fix (Try This First)

1. Right-click on `pom.xml`
2. Select **Maven → Reload project**
3. Wait 2-5 minutes for dependencies to download
4. **Build → Rebuild Project**

## If Dependencies Won't Download

Make sure you have internet connection, then:

### Check Maven Settings

1. **Settings → Build, Execution, Deployment → Build Tools → Maven**
2. Verify:
   - **Maven home directory**: "Bundled (Maven 3.x)" or path to your Maven
   - **User settings file**: Can be empty or point to settings.xml
   - **Local repository**: Should show path like `/Users/armengevorgyan/.m2/repository`

### Force Dependency Update

In IntelliJ Terminal (bottom):
```bash
# If you don't have Maven installed, this won't work
# But IntelliJ should do this automatically

# Check if pom.xml is valid
cat pom.xml
```

## Expected Behavior

After fixing:

✅ No build errors
✅ Maven dependencies downloaded to External Libraries
✅ Can build the project successfully
✅ Can run `DubaiLicenseTheoryApplication.java`

## Run the Application

Once dependencies are downloaded:

1. Navigate to: `src/main/java/com/dubai/dlt/DubaiLicenseTheoryApplication.java`
2. **Right-click** on the file
3. Select **"Run 'DubaiLicenseTheoryApplication'"**
4. Application should start on port 8080

## Troubleshooting

### Still Getting Build Errors?

Try this complete reset:

```bash
# In IntelliJ Terminal or external terminal
cd /Users/armengevorgyan/IdeaProjects/DLT
rm -rf target
```

Then in IntelliJ:
1. **File → Invalidate Caches → Invalidate and Restart**
2. After restart: **Build → Rebuild Project**

### Dependencies Not Downloading?

1. Check Maven tool window (right side)
2. Expand **Dependencies** - should show list of dependencies
3. If empty or shows errors:
   - Click **↻ Reload All Maven Projects**
   - Check internet connection
   - Check if you're behind a corporate firewall/proxy

## Settings Summary

Here's what your IntelliJ should be configured as:

**Settings → Build, Execution, Deployment → Build Tools → Maven:**

- **Maven home directory:** Bundled (Maven 3.x)
- **User settings file:** (can be empty)
- **Local repository:** /Users/armengevorgyan/.m2/repository

**Importing:**
- ✅ Import Maven projects automatically
- ✅ Automatically download: Sources
- ✅ Automatically download: Documentation

**Runner:**
- ✅ Delegate IDE build/run actions to Maven

Then click **Apply → OK** and reload Maven project.
