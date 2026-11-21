# Gowaru provider integration notes

Upstream project: https://github.com/Gowaru/gowaru-nuvio-providers

The upstream repository is licensed under GPL-3.0. Preserve its LICENSE and all source-level notices before redistributing any upstream files. Record the exact upstream commit and file provenance in THIRD_PARTY_NOTICES.md when code is imported.

## Compatibility boundary

Gowaru providers use a JavaScript provider contract that returns stream candidates for a media identifier and episode coordinates. This Android app currently defines the Kotlin-side normalized contract in `providers/Provider.kt`; the JavaScript runtime/bridge and upstream provider bundle have not yet been vendored.

Before importing providers:
1. Review each provider's target service and terms.
2. Enable only sources for which the app operator has authorization.
3. Do not include DRM circumvention, paywall bypass, credential theft, or access-control evasion.
4. Preserve provider attribution, GPL-3.0 licensing, and third-party notices.
5. Add tests for manifest parsing, provider isolation, stream normalization, headers, and subtitles.

The metadata catalogue and stream resolver are separate systems. A catalogue (for example, a configured TMDB integration) supplies titles, artwork, IDs, seasons, and episode metadata; providers resolve only authorized playback sources.
