# FreeFlix

Native Android application for phones and tablets, built with Kotlin, Jetpack Compose and AndroidX Media3.

## Current implementation

- Native Android shell with dark, responsive Compose UI.
- TMDB-powered trending movies, popular TV and multi-search.
- French metadata, posters, backdrops and detail summaries.
- Provider registry boundary and normalized stream/subtitle models.
- Media3 dependencies are present for the playback layer.

## Configure the catalogue

Create or edit `local.properties` at the project root (never commit this file):

```properties
TMDB_API_KEY=your_tmdb_v3_api_key
```

Alternatively set the `TMDB_API_KEY` environment variable or Gradle property. The key is injected into BuildConfig at build time.

TMDB supplies catalogue metadata and artwork. It does not supply the video streams.

## Gowaru

Upstream: https://github.com/Gowaru/gowaru-nuvio-providers

The app's provider contract is intentionally separate from the catalogue. Before enabling any upstream provider, preserve its license and notices and verify the relevant source is authorized for the content and territory. The current commit does not yet embed Gowaru's provider bundle or claim that its scrapers are active.

## Build

Requirements: JDK 17, Android SDK 36, Gradle wrapper.

```bash
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

## License

Application code: GPL-3.0-or-later. Third-party components retain their own licenses.
