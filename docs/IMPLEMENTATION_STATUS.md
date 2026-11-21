# FreeFlix implementation status

Last updated: 2026-11-21

## Implemented in repository
- Native Android project scaffold (Kotlin, Compose, Material 3).
- TMDB catalogue API client for trending movies and popular TV.
- Multi-search and basic title detail presentation.
- TMDB API key configuration via Gradle property, environment or local.properties.
- Kotlin provider interface and normalized stream/subtitle models.
- Media3 dependencies declared.
- Agent handoff instructions in `AGENTS.md`.
- Android CI workflow added; it requires a Gradle wrapper to be committed.

## Not yet implemented or verified
- Gowaru provider bundle is not vendored.
- JavaScript runtime and Kotlin bridge are not implemented.
- Season/episode browsing is not implemented.
- Source discovery and source selection UI are not implemented.
- Media3 player screen and playback controls are not implemented.
- Gradle wrapper is not confirmed present.
- No successful clean build or device playback test has been recorded.

## Next owner-agent tasks
Follow `AGENTS.md` in order. Start by adding/verifying the Gradle wrapper and making the current project compile. Then implement the Gowaru adapter/runtime, source UI, episode navigation and Media3 playback, observing the authorization and access-control requirements in that file.
