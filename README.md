# Termix

**A minimal no-root Linux terminal for Android.**

Termix lets you pick a Linux distribution, installs it via PRoot, and gives you a Termux-style multi-session terminal.

- **No AI**
- **No agent**
- **No extra features**
- Just PRoot + terminal (up to 20 sessions)

## Features (v0.1.0 skeleton)

| Feature | Status |
|---------|--------|
| Distro selection (Ubuntu / Debian / Alpine / Arch) | ✅ UI ready |
| RootFS install flow | 🟡 Stub (simulates download) |
| Multi-session terminal (max 20) | ✅ UI + manager ready |
| Termux-style extra keys | ✅ UI ready |
| Real PRoot binary | ❌ Drop `libproot.so` into `jniLibs/arm64-v8a/` |
| Real OCI / tarball RootFS download | ❌ Replace stub in `ProotInstaller` |
| Real PTY (Termux terminal-view or JNI) | ❌ Next step |

## Requirements

- Android 8.0+ (API 26)
- **arm64-v8a** device
- Network (for RootFS download)

## Project structure

```
Termix/
├── app/
│   └── src/main/java/com/termix/
│       ├── app/           # Application + MainActivity
│       ├── core/
│       │   ├── proot/     # Distro catalog, installer, prefs
│       │   ├── session/   # SessionManager (max 20)
│       │   └── pty/       # Command builder for proot shell
│       └── ui/
│           ├── onboarding/  # Distro picker
│           ├── terminal/    # Multi-session terminal UI
│           └── theme/
├── app/src/main/jniLibs/arm64-v8a/   # ← put libproot.so here
└── README.md
```

## Build

```bash
# From project root (needs Android SDK + JDK 17)
./gradlew assembleDebug
```

APK output:

```
app/build/outputs/apk/debug/app-debug.apk
```

## Next steps for a working terminal

1. **Add PRoot binary**
   - Get a static ARM64 `proot` (e.g. from Termux packages or green-green-avk builds)
   - Place as `app/src/main/jniLibs/arm64-v8a/libproot.so`

2. **Real RootFS install**
   - Replace the stub in `ProotInstaller.install()` with:
     - Download Ubuntu/Debian/Alpine ARM64 rootfs tarball (or OCI layers)
     - Extract into `context.filesDir/rootfs/<distro-id>/`
     - Write `resolv.conf`, basic profile, etc.

3. **Wire a real PTY**
   - Option A: Embed Termux `terminal-emulator` + `terminal-view` libraries
   - Option B: Use JNI `openpty` (like TaiXu’s `pty_native.c`)
   - Feed `PtyLauncher.buildProotShellCommand(...)` into the PTY backend

4. **Optional polish**
   - Persist session list across process death
   - Font size / color scheme settings
   - Keep-screen-on while terminal is open

## License

MIT (you can change this)

## Credits / inspiration

- [Termux](https://termux.dev) – terminal-view & PTY approach
- [TaiXu](https://github.com/wkbin/taixu) – high-quality PRoot + onboarding flow
- proot-distro – OCI RootFS idea
