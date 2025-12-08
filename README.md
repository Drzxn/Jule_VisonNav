# Indoor Navigation App

This is a comprehensive indoor navigation application for Android, built with Kotlin, Jetpack Compose, ARCore, and a suite of machine learning models for real-time scene understanding and hazard detection.

## Project Structure

The project follows a clean architecture (MVVM) with a modular approach.

- `app/src/main/java/com/example/indoornavigation`: Root package.
  - `data`: Contains data sources, repositories, and models.
    - `database`: Room database for offline map storage.
    - `model`: Data models for the application.
    - `repository`: Repositories to abstract data sources.
  - `domain`: Contains use cases that encapsulate business logic.
  - `ui`: Contains the UI layer, built with Jetpack Compose.
    - `screens`: Composable screens for different parts of the app.
    - `viewmodel`: ViewModels for each screen.
    - `theme`: Theming for the app.
  - `camerax`: Manages the camera feed.
  - `ml`: Contains the machine learning models and inference code.
  - `ar`: Manages the ARCore session and SLAM.
  - `hazard`: Detects hazards by fusing ML and AR data.
  - `navigation`: The navigation engine for pathfinding.
  - `feedback`: Audio and haptic feedback engines.
  - `map`: Tools for building and managing indoor maps and POIs.
  - `service`: A foreground service for continuous navigation.

## How to Build

1. **Clone the repository.**
2. **Open the project in Android Studio.**
3. **Replace the placeholder `app/google-services.json` with your own Firebase configuration file.**
4. **Place your TensorFlow Lite models (`.tflite`) in the `app/src/main/assets` directory.**
5. **Build and run the project.**

## Dependencies

- **Jetpack Compose**: For the UI.
- **ARCore**: For SLAM and depth sensing.
- **CameraX**: For the camera feed.
- **TensorFlow Lite**: For on-device machine learning.
- **Hilt**: For dependency injection.
- **Room**: For local database storage.
- **Firebase**: For backend services (Firestore, Storage).
