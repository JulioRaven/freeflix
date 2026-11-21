# Gowaru integration

Upstream: https://github.com/Gowaru/gowaru-nuvio-providers

Gowaru is a JavaScript provider collection, while FreeFlix is a native Kotlin/Android app. The adapter boundary is `MediaProvider.getStreams(MediaRequest)`, with normalized `MediaStream` and `SubtitleTrack` models.

## Integration plan

1. Pin and record the exact upstream commit and license.
2. Inspect the upstream manifest and provider contract; do not assume every provider has the same capabilities.
3. Add a JavaScript runtime/bridge compatible with the provider bundle's supported JS subset.
4. Convert the app's TMDB/IMDb identifiers and episode coordinates to the upstream request format.
5. Normalize returned stream candidates, headers, language, quality and subtitles.
6. Present source candidates to the user and hand selected authorized media URLs to Media3.
7. Add provider isolation, timeout, parsing, and playback tests.

## Important separation

TMDB powers title discovery and metadata. Gowaru-style providers resolve playback candidates; they are not the catalogue itself.

Only activate sources that are authorized to provide the requested content. Do not implement DRM circumvention, paywall bypass, or access-control evasion. Do not claim that a provider works until it has been integrated and tested on-device.

## Current status

The current app has a working catalogue client and a Kotlin provider contract. The Gowaru JavaScript bundle and runtime bridge are not yet embedded.
