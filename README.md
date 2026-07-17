# ⚡ Network Launcher (Radio Info Shortcut)

[![Android SDK](https://img.shields.io/badge/SDK-24%20--%2036-brightgreen.svg)](#project-architecture)
[![Language](https://img.shields.io/badge/Language-Kotlin-orange.svg)](#project-architecture)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](#license)

An ultra-lightweight, zero-latency Android utility application designed to act as a direct shortcut to the hidden system **Radio Info** settings screen (`*#*#4636#*#*`). 

Many modern Android devices, carriers, and custom dialer applications block or disable the classic dialer codes used to access advanced cell configuration menus. This app bypasses those limitations, launching the configuration page instantly.

---

## ✨ Features

- **🚀 Instant Execution**: Launches the advanced radio settings activity instantly during `onCreate` and finishes immediately—offering a completely native integration feel.
- **🎨 Modern Adaptive Icon**: Features a high-definition vector launcher icon (clean white background with a contrasting black telephone receiver dial) that scales perfectly on modern Android launchers.
- **🔒 Privacy First (Zero Permissions)**: Unlike older shortcut apps, this launcher does not read phone states or request any runtime device permissions.
- **⚡ Zero Overhead**: Rewritten fully in Kotlin, utilizing optimized transparent transition lifecycles to eliminate cold-start lag and visual window flashes.

---

## 🛠️ Project Architecture

| Component | Target Version / Tech |
| :--- | :--- |
| **Language** | Kotlin |
| **Compile SDK** | `36` (Android 16 preview) |
| **Target SDK** | `35` (Android 15) |
| **Minimum SDK** | `24` (Android 7.0 Nougat) |
| **Build System** | Gradle (Kotlin DSL, Version `9.1.0`) |
| **CI/CD** | GitHub Actions (Dynamic versioning tag releases) |

---

## 📂 Project Structure

```
Network/
├── .github/workflows/
│   └── build.yml               # Automated APK compilation and GitHub Release workflow
├── app/
│   ├── src/main/
│   │   ├── java/de/mangelow/network/
│   │   │   └── RadioNetwork.kt # Main Activity (instant launcher execution)
│   │   ├── res/
│   │   │   ├── drawable/
│   │   │   │   └── ic_launcher_foreground.xml # Scalable vector phone handset dial
│   │   │   ├── mipmap-anydpi-v26/
│   │   │   │   ├── ic_launcher.xml            # Adaptive launcher icon mapping
│   │   │   │   └── ic_launcher_round.xml      # Round launcher icon mapping
│   │   │   ├── values/
│   │   │   │   ├── colors.xml                 # Icon palette configuration
│   │   │   │   └── themes.xml                 # Custom transparent transition theme
│   │   │   └── xml/
│   │   │       ├── backup_rules.xml
│   │   │       └── data_extraction_rules.xml
│   │   └── AndroidManifest.xml  # Core application manifest (zero permissions declared)
│   └── build.gradle.kts        # App Gradle configurations
├── build.gradle.kts            # Project root Gradle configurations
├── settings.gradle.kts         # Repository settings
└── gradle.properties           # Gradle JVM and caching options
```

---

## ⚙️ How It Works Under The Hood

To guarantee zero delay when launching the settings page, the app overrides standard Android Activity startup behaviors:

1. **Transparent Theme Preview**: Setting the theme of `RadioNetwork` to `Theme.Translucent.NoTitleBar` and setting `android:windowDisablePreview` to `true` disables the standard starting window flash.
2. **Transition Override**: The app immediately calls `overrideActivityTransition` or `overridePendingTransition` to eliminate the entry and exit slide animations.
3. **Multi-Intent Target Matching**: It sequentially tries multiple known vendor-specific package paths to support diverse device configurations (e.g. standard Pixel AOSP `com.android.settings`, OEM phone apps `com.android.phone`, etc.).

---

## 📦 How to Build and Run

### Prerequisites
- Android SDK installed (`sdk.dir` configured in your `local.properties` file).
- Java Development Kit (JDK) 17.

### Build Compilation
Generate the debug APK:
```powershell
./gradlew assembleDebug
```

### Installation
Deploy directly to a connected ADB device:
```powershell
./gradlew installDebug
```

---

## 📄 License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](file:///c:/Users/akash/Downloads/Project/Network/LICENSE) file for the full text.
