# Build Instructions

## Method 1: Android Studio (Easiest)

1. **Install Android Studio**
   - Download from: https://developer.android.com/studio
   - Install and run first-time setup

2. **Open Project**
   ```
   File → Open → Select neural-shell-android folder
   ```

3. **Wait for Gradle Sync**
   - Android Studio will automatically download dependencies
   - This takes 5-10 minutes first time

4. **Build APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

5. **Find APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

6. **Install on Phone**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

---

## Method 2: Command Line (Advanced)

### Prerequisites
```bash
# Install JDK 17
sudo apt install openjdk-17-jdk

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

### Download Gradle Wrapper JAR
```bash
cd neural-shell-android
wget https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar \
  -O gradle/wrapper/gradle-wrapper.jar
```

### Build
```bash
chmod +x gradlew
./gradlew assembleDebug
```

### Install
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## After Installation

### 1. Add Claude API Key
Edit `app/src/main/java/com/neuralshell/android/ai/ClaudeService.kt`:
```kotlin
private const val API_KEY = "sk-ant-YOUR_KEY_HERE"
```

Then rebuild.

### 2. Enable Accessibility Service
1. Open Settings on your phone
2. Go to Accessibility
3. Find "Neural Shell"
4. Toggle ON

### 3. Grant Permissions
- Battery optimization: Settings → Apps → Neural Shell → Battery → Unrestricted
- Display over other apps: Settings → Apps → Neural Shell → Display over other apps → Allow

---

## Troubleshooting

### Build Fails - Missing SDK
```bash
# In Android Studio:
Tools → SDK Manager → Install Android 14.0 (API 34)
```

### Gradle Sync Fails
```bash
# Clean and rebuild
./gradlew clean
./gradlew assembleDebug
```

### APK Won't Install
```bash
# Uninstall old version first
adb uninstall com.neuralshell.android
adb install app/build/outputs/apk/debug/app-debug.apk
```
