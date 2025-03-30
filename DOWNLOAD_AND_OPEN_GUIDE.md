# How to Download and Open Project in Android Studio

## 1. Download the Project
1. Get the project files:
   - If using GitHub: `git clone https://github.com/[your-repo]/ExpiryDateTracker.git`
   - If using ZIP: Download and extract to your projects folder

## 2. Open in Android Studio
1. Launch Android Studio
2. On welcome screen, select "Open" (or File > Open for existing IDE window)
3. Navigate to the extracted project folder
4. Select the root folder containing:
   - `app/` directory
   - `build.gradle` files
   - `gradle/` directory
5. Click "Open"

## 3. First Time Setup
1. Wait for Gradle sync to complete (may take several minutes)
2. Accept any prompts to install missing components
3. Verify no errors in Build output tab

## 4. Project Structure Verification
Ensure these files exist:
- `app/build.gradle`
- `app/google-services.json` (after Firebase setup)
- `gradle/wrapper/gradle-wrapper.properties`