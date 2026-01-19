# Neural Shell Android

AI-Native Android Terminal with natural language command parsing and screen context awareness.

## Features

- **PTY Terminal** - Full shell execution via `/system/bin/sh`
- **Natural Language Parsing** - Claude AI converts "show running processes" → `ps aux`
- **Accessibility Service** - Screen context awareness for AI
- **Agent Mode** - Background automation (WIP)
- **Touch-Optimized UI** - Extra keys bar, Jetpack Compose

## Build Instructions

### Prerequisites
- Android Studio Iguana or later
- JDK 17
- Android SDK 34

### Setup
1. Clone the repository:
```bash
git clone https://github.com/pistakugli/neural-shell-android.git
cd neural-shell-android
```

2. Add your Claude API key in `ClaudeService.kt`:
```kotlin
private const val API_KEY = "YOUR_API_KEY_HERE"
```

3. Build APK:
```bash
./gradlew assembleDebug
```

4. Install on device:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Enable Accessibility Service
1. Open Settings → Accessibility
2. Find "Neural Shell"
3. Enable the service

## Architecture

```
app/
├── ai/
│   ├── ClaudeModels.kt     # API data models
│   └── ClaudeService.kt    # Retrofit API client
├── terminal/
│   ├── PtyProcess.kt       # PTY process wrapper
│   └── TerminalSession.kt  # Session management
├── accessibility/
│   └── NeuralAccessibilityService.kt  # Screen context
├── agent/
│   └── AgentService.kt     # Background automation
└── ui/
    ├── TerminalScreen.kt   # Compose UI
    ├── viewmodel/
    │   └── TerminalViewModel.kt
    └── theme/
        └── Theme.kt
```

## Usage

**Natural Language Commands:**
```
> show running processes
🤖 Parsing: show running processes
→ ps aux
[output]
```

**Direct Shell Commands:**
```
> ls -la
[output]
```

## Requirements
- Android 8.0+ (API 26+)
- 4GB RAM minimum
- Internet connection for AI parsing

## License
MIT

## TODO
- [ ] MLKit OCR integration
- [ ] Gesture controls
- [ ] Agent mode automation
- [ ] Local LLM fallback
- [ ] Command history
- [ ] Multiple sessions
