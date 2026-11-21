# Agent instructions — FreeFlix

You are working in the existing native Android project. Continue from the current branch; do not replace the app with a website, PWA, WebView wrapper, or unrelated starter template.

## Product goal

Build **FreeFlix**, a native Android application for phones and tablets, with a polished streaming-discovery experience:
- Discover films, TV series and anime in a searchable catalogue.
- Open rich title details and browse seasons/episodes.
- Display available provider/source choices for a selected title.
- Play selected authorized streams inside the app using AndroidX Media3 (ExoPlayer).
- Support subtitles, audio tracks when exposed by the stream, quality/source selection, fullscreen, playback resume and useful error states.
- French-first UI, responsive Compose layouts for phones and tablets.

## Current repository state

- Kotlin + Jetpack Compose + Material 3 Android app.
- TMDB catalogue client and search/detail UI in `app/src/main/java/com/freeflix/app/catalog/Catalog.kt` and `MainActivity.kt`.
- TMDB key is read from Gradle property, environment variable or local.properties as `TMDB_API_KEY`.
- Media3 dependencies are declared.
- A normalized Kotlin provider contract exists under `app/src/main/java/com/freeflix/app/providers/`.
- Gowaru upstream has **not** yet been embedded and the JavaScript runtime/bridge is not implemented.
- Do not claim the app is buildable or playback-ready until you actually run and verify the build and device/emulator tests.

## Gowaru integration handoff

Upstream project: https://github.com/Gowaru/gowaru-nuvio-providers

Treat Gowaru as the intended provider ecosystem, not as a catalogue API. TMDB remains responsible for catalogue metadata; provider adapters are responsible for returning stream candidates.

1. Inspect the upstream README, manifest, provider contract, license and current source tree. Pin the exact upstream commit used.
2. Preserve GPL-3.0 and all upstream copyright/license notices. Update `THIRD_PARTY_NOTICES.md` with exact commit and provenance for every imported component.
3. Implement an isolated JavaScript runtime/bridge compatible with the provider bundle's documented runtime contract. Keep JavaScript execution off the main thread; add timeouts, cancellation, bounded memory, structured errors and provider isolation.
4. Implement a Kotlin adapter that maps FreeFlix media identifiers and episode coordinates into the provider request contract, then normalizes returned stream metadata into `MediaStream` and `SubtitleTrack`.
5. Add a source-selection UI. Do not silently autoplay an arbitrary source; show provider, language and quality where available.
6. Pass a selected supported stream to Media3. Handle headers, HLS/DASH/progressive formats, subtitles and playback errors explicitly.
7. Add unit tests for manifest parsing, bridge serialization, stream normalization, provider failures, timeout/cancellation and subtitle mapping.
8. Add integration tests using a clearly authorized sample stream or public-domain media. Do not use a copyrighted title as a test fixture unless the project owner has documented permission.

## Authorization and access controls

Only enable providers and media sources for which the project operator has documented authorization to access and distribute the requested content in the relevant territory. Do not implement DRM circumvention, paywall/subscription bypass, credential theft, or evasion of authentication or other access controls. If a provider requires such a bypass, leave it disabled and document the reason rather than attempting to defeat it.

Do not assume that a provider repository's existence or open-source license grants rights to every third-party film, series, anime, stream host, or territory. Keep the provider runtime generic and make source enablement explicit.

## Required engineering workflow

- First inspect the current code and build configuration; preserve existing work.
- Fix compile errors before adding major features.
- Use stable, maintained Android libraries and compatible versions.
- Keep secrets out of Git. Never commit `local.properties`, API keys, tokens or credentials.
- Add a Gradle wrapper if missing and verify `./gradlew assembleDebug`.
- Add GitHub Actions to build the debug APK and upload it as an artifact.
- Run tests and report exact commands/results. Do not claim tests passed unless they were run.
- Keep README and implementation status accurate.
- Commit work in small, descriptive commits.

## Acceptance criteria

- [ ] Debug APK builds successfully from a clean checkout.
- [ ] TMDB catalogue, search and details work with a configured key.
- [ ] Films, series and anime are clearly distinguished in the UI.
- [ ] Seasons and episodes can be browsed for TV titles.
- [ ] Provider runtime can load the pinned provider definitions safely.
- [ ] Source candidates are normalized and displayed.
- [ ] Authorized streams play in the in-app Media3 player.
- [ ] Subtitles, fullscreen, seeking and playback errors are handled.
- [ ] Phone and tablet layouts are usable.
- [ ] License notices and provider provenance are complete.
- [ ] README reflects the actual tested state.
