# Sante-Price Index Android App

This folder is now an Android Studio project written in Kotlin with Jetpack Compose.

## What is included

- Google-only sign-in entry screen. No OTP, phone, or SMS login feature is included.
- Market onboarding for Bangalore, Mumbai, Chennai, Pune, and Delhi.
- Price Watch dashboard with local mandi price data.
- Profit Calculator using the cost-plus formula from `README 2.md`.
- Market Trends screen with a lightweight 7-day chart and forecast text.
- Digital Price Board with yellow-on-black high contrast, font size controls, full brightness, and keep-screen-on behavior.
- Unit test for the pricing engine.

## Open in Android Studio

1. Open Android Studio.
2. Choose `Open`.
3. Select this folder: `C:\Users\User\OneDrive\Desktop\Sante-price index`.
4. Let Gradle sync.
5. Run the `app` configuration on an emulator or Android phone.

## Google Sign-In setup

The project uses Android Credential Manager and Google ID.

For production sign-in:

1. Create an OAuth web client ID in Google Cloud Console.
2. Replace `YOUR_WEB_CLIENT_ID.apps.googleusercontent.com` in `app/src/main/res/values/strings.xml`.
3. Add your Android package name and SHA certificate fingerprints to the same Google project.

Until that client ID is replaced, the button opens a preview vendor session so you can test the full app flow in Android Studio.

## Firebase

The current app uses local sample data so it can open immediately without a Firebase project. The data layer is intentionally separated in `MarketRepository.kt`, so Firebase Firestore or Realtime Database can replace the local repository later without changing the screens.

Recommended next Firebase additions:

- Add Firebase Authentication after Google Cloud credentials are ready.
- Add Firestore collections for `users`, `prices`, and `history`.
- Replace `MarketRepository.pricesFor()` with a real-time listener.
- Persist `activeMarket` and board prices for each signed-in user.
