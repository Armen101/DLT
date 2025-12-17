# How to Enable Maven in IntelliJ

## The Issue

You can't see the Maven tool window because IntelliJ doesn't recognize this as a Maven project yet.

## Solution: Import as Maven Project

### Method 1: Add Maven Framework (RECOMMENDED)

1. **Right-click on the project root folder** "DLT" in the Project view (left side)
2. Select **"Add Framework Support..."**
3. In the dialog, check **Maven**
4. Click **OK**
5. IntelliJ will now recognize it as a Maven project
6. The Maven tool window should appear on the right side

### Method 2: Import pom.xml Directly

1. **Right-click on `pom.xml`** in the Project view
2. Select **"Add as Maven Project"**
3. Wait for IntelliJ to reload
4. Maven tool window will appear

### Method 3: Reimport the Project (MOST RELIABLE)

1. **Close IntelliJ completely**
2. **Reopen IntelliJ**
3. Select **File → Open**
4. Navigate to: `/Users/armengevorgyan/IdeaProjects/DLT`
5. Select the **DLT** folder
6. Click **Open**
7. When prompted **"Maven project detected"**, click **Import**
8. OR: A notification will appear saying "Maven projects need to be imported"
   - Click **"Import Changes"** or **"Enable Auto-Import"**

### Method 4: Manual Maven Tool Window

If the tool window is hidden:

1. Go to **View → Tool Windows → Maven**
2. The Maven window should appear on the right
3. If you see "No Maven projects found":
   - Click the **+** button (Add Maven Projects)
   - Navigate to and select your `pom.xml` file
   - Click **OK**

### Method 5: Project Structure

1. **File → Project Structure** (or press Cmd+;)
2. Go to **Modules**
3. Click the **-** (minus) button to remove existing module
4. Click the **+** (plus) button
5. Select **Import Module**
6. Navigate to `/Users/armengevorgyan/IdeaProjects/DLT/pom.xml`
7. Select **Import module from external model → Maven**
8. Click **Finish**

## After Maven is Recognized

Once Maven is enabled, you should see:

1. **Maven tool window** on the right side showing:
   ```
   DLT
   ├── Lifecycle
   │   ├── clean
   │   ├── validate
   │   ├── compile
   │   └── ...
   └── Dependencies
       └── (list of dependencies)
   ```

2. **Then download dependencies:**
   - Click the **↻ Reload All Maven Projects** icon at the top of Maven window
   - Wait 2-5 minutes for download
   - See "BUILD SUCCESS"

## Complete Reset Method

If nothing works, do a complete reset:

### Step 1: Close IntelliJ

### Step 2: Delete IntelliJ Configuration
```bash
cd /Users/armengevorgyan/IdeaProjects/DLT
rm -rf .idea
rm -rf *.iml
rm -rf target
rm -rf out
```

### Step 3: Open Project Fresh
1. Open IntelliJ
2. **File → Open**
3. Select the DLT folder
4. **IMPORTANT:** When you see "IntelliJ IDEA found a pom.xml file"
   - Select **"Open as Maven Project"**
   - OR click the notification to import

### Step 4: Wait for Indexing
- Bottom status bar shows "Indexing..."
- Wait for it to complete

### Step 5: Import Maven Project
- A notification appears: "Maven projects need to be imported"
- Click **"Import Changes"** or **"Enable Auto-Import"**
- Wait for dependencies to download

## Verify Maven is Working

### Check 1: Maven Tool Window Visible
- Look at right side of IntelliJ
- You should see "Maven" tab
- Click it to expand the Maven tool window

### Check 2: pom.xml has Maven Icon
- In Project view, `pom.xml` should have a special Maven icon (letter M)

### Check 3: Project Structure Shows Maven
- File → Project Structure → Modules
- The module should show "Maven" next to it

### Check 4: Dependencies Folder Exists
- In Maven tool window
- You should see "Dependencies" folder (might be empty until you reload)

## Quick Commands to Run

### Option A: Use IntelliJ Terminal

Open Terminal in IntelliJ and run:
```bash
# Test if Maven recognizes the project
ls pom.xml

# Try to resolve dependencies manually
mvn help:effective-pom
```

If you get "mvn: command not found", that's OK - IntelliJ has its own bundled Maven.

### Option B: Delete and Recreate .idea

```bash
# In Terminal (in IntelliJ)
rm -rf .idea *.iml target out
```

Then restart IntelliJ and open the project again.

## Expected Result

After following these steps, you should have:

✅ Maven tool window visible on the right side
✅ pom.xml recognized with Maven icon
✅ Ability to click "Reload All Maven Projects"
✅ Dependencies will download (~200 MB, takes 2-5 minutes)
✅ All Spring Boot imports will work (no red underlines)

## Still Not Working?

Try this diagnostic:

1. **File → Settings** (Preferences on Mac)
2. **Build, Execution, Deployment → Build Tools → Maven**
3. Verify:
   - Maven home directory: Should show "Bundled (Maven 3.x)"
   - User settings file: Can be blank
   - Local repository: Should show path to `.m2/repository`
4. Click **Apply → OK**

5. Then: **File → Invalidate Caches → Invalidate and Restart**

## Last Resort: Manual Dependency Download

If you have Maven installed separately:

```bash
cd /Users/armengevorgyan/IdeaProjects/DLT
mvn clean install -U
```

Then reopen IntelliJ and it should detect the downloaded dependencies.

---

**TL;DR - Fastest Method:**
1. Close IntelliJ
2. Delete `.idea` folder
3. Reopen IntelliJ
4. File → Open → Select DLT folder
5. When prompted about Maven → Click "Import"
6. Wait for Maven window to appear
7. Click Reload button in Maven window
