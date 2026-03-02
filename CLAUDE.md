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

### Modules
- **`:app`** — Main application module (Java 11, minSdk 24, compileSdk 36)
- **`:sdk`** — OpenCV 4.13.0 Android SDK (included as a local module, not yet used by the app)

### Native Code (JNI)
The app includes a C++ native library (`app_corte1_demo`) built via CMake 3.22.1:
- Native source: `app/src/main/cpp/native-lib.cpp`
- CMake config: `app/src/main/cpp/CMakeLists.txt`
- The library is loaded in `MainActivity` via `System.loadLibrary("app_corte1_demo")`
- Native method: `stringFromJNI()` — currently returns a hardcoded string

### UI Layer
- Single `MainActivity` as the entry point
- View Binding is enabled — access views via the generated binding class (e.g., `ActivityMainBinding`)
- Layout: `app/src/main/res/layout/activity_main.xml` (ConstraintLayout)
- Dark mode supported via `values-night/themes.xml`

### Key Conventions
- Java 11 (`sourceCompatibility`/`targetCompatibility` both set to `VERSION_11`)
- Dependencies managed via Gradle version catalog at `gradle/libs.versions.toml`
- Non-transitive R classes enabled (`android.nonTransitiveRClass=true` in `gradle.properties`)

### OpenCV Integration
The OpenCV SDK is available as module `:sdk`. To use OpenCV in the app:
1. Add `implementation project(':sdk')` to `app/build.gradle` dependencies
2. Load OpenCV via `OpenCVLoader.initLocal()` before use
3. CMake is already set up to link against OpenCV native libraries
