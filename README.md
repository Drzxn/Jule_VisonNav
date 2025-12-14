# Real-Time Safety App (Phase 1)

This is a Phase-1 Android application that uses the device's camera to provide real-time AI safety alerts.

## Features

- Live camera preview using CameraX.
- Real-time object detection using a YOLOv8n TFLite model.
- Approximate distance estimation using the ARCore Depth API.
- A simple, rule-based hazard engine to trigger audio warnings.
- Audio feedback using TextToSpeech.
- A foreground service to ensure the app continues running in the background.

## How to Build

1.  **Clone the repository.**
2.  **Replace the placeholder model:** The file `app/src/main/assets/yolov8n.tflite` is a placeholder. You must replace it with a valid, INT8 quantized YOLOv8n TFLite model for the application to function correctly.
3.  **Open the project in Android Studio.**
4.  **Build and run the project.**

## Architecture

The project follows a clean architecture with a simplified MVVM pattern, where the core logic resides in a foreground service.

-   `service`: Contains the `ProcessingService`, which is the heart of the application.
-   `camera`: Manages the CameraX pipeline.
-   `ml`: Handles TFLite model loading and inference.
-   `ar`: Manages the ARCore session and depth estimation.
-   `domain`: Contains the hazard detection logic.
-   `audio`: Manages the TextToSpeech engine.
-   `ui`: Contains the Jetpack Compose UI and the ViewModel.
