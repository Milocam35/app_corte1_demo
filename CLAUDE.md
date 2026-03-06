# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install debug APK on connected device/emulator
./gradlew installDebug

# Clean build artifacts
./gradlew clean

# Run all unit tests
./gradlew test

# Run a single unit test class
./gradlew :app:testDebugUnitTest --tests "com.example.app_corte1_demo.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Run lint checks
./gradlew lint
```

## Architecture

### App Purpose
Image processing demo app that lets users pick an image from their gallery and apply OpenCV filters (grayscale conversion, Gaussian glow effect) via C++ native code.

### Modules
- **`:app`** — Main application module (Java 11, minSdk 24, compileSdk 36)
- **`:sdk`** — OpenCV 4.13.0 Android SDK (local module, used by `:app`)
- **`:OpenCV`** — Additional OpenCV module (included in settings.gradle)

### Native Code (JNI)
The app includes a C++ native library (`app_corte1_demo`) built via CMake 3.22.1:
- Native source: `app/src/main/cpp/native-lib.cpp`
- CMake config: `app/src/main/cpp/CMakeLists.txt`
- The library is loaded in `MainActivity` via `System.loadLibrary("app_corte1_demo")`
- Native methods declared in `MainActivity`:
  - `convertToGray(long inputMat, long outputMat)` — RGBA to grayscale conversion
  - `applyExoticGaussian(long inputMat, long outputMat)` — Gaussian blur glow effect with brightness adjustment
- JNI functions receive `Mat` addresses as `jlong` and cast them to `cv::Mat*`
- CMake links against OpenCV native libs via `${OpenCV_DIR}` pointing to `sdk/native/jni`

### UI Layer
- Single `MainActivity` as the entry point
- View Binding enabled — access views via `ActivityMainBinding`
- Layout: `app/src/main/res/layout/activity_main.xml` (ConstraintLayout)
- UI elements: image picker button, ImageView for display, grayscale filter button, Gaussian filter button
- Image picker uses `ActivityResultContracts.GetContent` with `"image/*"` MIME type
- Dark mode supported via `values-night/themes.xml`

### OpenCV Integration
OpenCV is already fully integrated:
- `:sdk` module is a dependency of `:app` (`implementation project(':sdk')`)
- Initialized at runtime via `OpenCVLoader.initDebug()` in `onCreate`
- Java-side: `org.opencv.android.Utils` converts between `Bitmap` and `Mat`
- Native-side: `opencv2/opencv.hpp` used for image processing (`cvtColor`, `GaussianBlur`, `addWeighted`)

### Key Conventions
- Java 11 (`sourceCompatibility`/`targetCompatibility` both set to `VERSION_11`)
- Dependencies managed via Gradle version catalog at `gradle/libs.versions.toml`
- Non-transitive R classes enabled (`android.nonTransitiveRClass=true` in `gradle.properties`)
- UI text is in Spanish (e.g., "Seleccionar Imagen", "Escala de Grises")
