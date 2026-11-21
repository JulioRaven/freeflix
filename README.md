# FreeFlix

Native Android streaming-discovery and playback app for phones and tablets.

> This repository is the Android application project. The APK is built by the project's GitHub Actions workflow or by the maintainer's build agent.

## Project direction

- Native Android app: Kotlin, Jetpack Compose, Material 3.
- Phone and tablet layouts; no web/PWA target.
- Media playback with AndroidX Media3 (ExoPlayer).
- Search and metadata catalogue separated from playback sources.
- Embedded provider-runtime architecture inspired by Gowaru's Nuvio provider format.
- Provider manifests, enable/disable controls, source diagnostics and stream normalization.
- French-first interface, with language-ready resources.

## Provider and copyright policy

The upstream [Gowaru Nuvio providers](https://github.com/Gowaru/gowaru-nuvio-providers) project is GPL-3.0. Any source code redistributed from it must retain the applicable license and notices. This app will only activate providers and integrations that are authorized for the content and territories in which they are used. It will not implement DRM circumvention, paywall bypass, or access-control evasion.

The provider runtime is designed to support documented, lawful providers and user-owned media sources. It does not imply that every third-party scraper from an upstream bundle is enabled or authorized.

## Build

Requirements:
- JDK 17
- Android SDK (compile SDK 36)
- Gradle wrapper

Build a debug APK:

```bash
./gradlew assembleDebug
```

APK output:

```
app/build/outputs/apk/debug/app-debug.apk
```

## Configuration

Add a TMDB API key to local.properties or the appropriate CI secret before enabling catalogue search. Never commit API secrets.

## License

Application code: GPL-3.0-or-later unless a file states otherwise. Third-party components retain their own licenses; see their notices and dependency metadata.
