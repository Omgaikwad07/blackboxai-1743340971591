# Android Studio Setup and Run Guide

## 1. Prerequisites
- Android Studio Hedgehog or newer
- Android SDK 33+
- Java 17+
- Physical Android device (recommended) or emulator (API 24+)

## 2. Import Project
1. Open Android Studio
2. Click "Open" (for existing project)
3. Navigate to project root folder
4. Select the `build.gradle` file
5. Click "Open"

## 3. Configure Project
1. Wait for Gradle sync to complete
2. Verify no errors in Build output
3. Check `google-services.json` exists in `app/` folder

## 4. Set Up Device
1. Connect Android device via USB (enable USB debugging)
   OR
2. Create virtual device:
   - Tools > Device Manager > Create Device
   - Select Pixel 5 (recommended)
   - Download API 33 (Android 13) image
   - Finish setup

## 5. Run the App
1. Select your device from dropdown (top toolbar)
2. Click green "Run" button (or Shift+F10)
3. Wait for build to complete
4. App will automatically install and launch

## 6. First Run Setup
1. Grant required permissions when prompted:
   - Camera (for barcode scanning)
   - Notifications
2. Create an account (Email/Password)
3. Start using the app features

## Troubleshooting
- If Gradle fails: File > Sync Project with Gradle Files
- If missing dependencies: Check internet connection
- If Firebase errors: Verify `google-services.json` is correct
- For build errors: Clean project (Build > Clean Project)