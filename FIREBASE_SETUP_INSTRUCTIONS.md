# Firebase Project Setup Guide

## 1. Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project"
3. Enter project name "ExpiryDateTracker"
4. Enable Google Analytics (optional)
5. Create project

## 2. Add Android App
1. In project overview, click Android icon
2. Enter package name: `com.example.expirydatetracker`
3. Enter app nickname (optional)
4. Click "Register app"

## 3. Download Config File
1. Download `google-services.json`
2. Place it in your project's `app/` directory

## 4. Enable Authentication
1. In Firebase Console, go to Authentication
2. Select "Sign-in method" tab
3. Enable Email/Password provider

## 5. Set Up Realtime Database
1. Go to Realtime Database
2. Click "Create database"
3. Start in test mode (for development)
4. Set location (choose closest to your users)

## 6. Security Rules (for development)
```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "auth != null && auth.uid == $uid",
        ".write": "auth != null && auth.uid == $uid"
      }
    }
  }
}
```

## 7. Testing Setup
1. Build and run the app
2. Verify you can:
   - Create user accounts
   - Add products
   - Receive notifications
   - Change settings

## Troubleshooting
- If authentication fails, verify Email/Password is enabled
- If database access fails, check security rules
- If notifications don't work, check FCM setup