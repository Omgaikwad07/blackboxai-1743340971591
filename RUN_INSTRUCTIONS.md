# How to Run the Expiry Date Tracker App

## Prerequisites
1. Android Studio (latest version)
2. Android device/emulator (API 24+)
3. Completed Firebase setup (google-services.json in place)

## Running the App

### Option 1: Android Studio
1. Open the project in Android Studio
2. Connect an Android device or start an emulator
3. Click "Run" (green play button) or press Shift+F10
4. Select your target device
5. Wait for build to complete and app to launch

### Option 2: Command Line
1. Ensure Android SDK is in your PATH
2. Navigate to project root
3. Run: `./gradlew installDebug`
4. Launch app from device/emulator

### First Run Setup
1. Create an account (Email/Password)
2. Grant required permissions:
   - Camera (for barcode scanning)
   - Notifications
3. Start adding products via:
   - Barcode scan (Scan tab)
   - Manual entry (Add Product tab)

## Testing Key Features
1. Scan a product barcode
2. Verify expiry notification appears at configured time
3. Check products appear in Home tab
4. Test notification settings in Settings

## Troubleshooting
- If app crashes on launch:
  - Verify google-services.json is in app/ directory
  - Check Firebase dependencies in build.gradle
- If barcode scan fails:
  - Verify camera permission is granted
  - Check device has a camera
- If notifications don't work:
  - Check notification settings
  - Verify Firebase Cloud Messaging is enabled