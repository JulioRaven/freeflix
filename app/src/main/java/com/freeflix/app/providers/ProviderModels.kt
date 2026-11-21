package com.freeflix.app.providers

/**
 * Normalized stream model used by the app/player boundary.
 * Only sources that are authorized for the requested media may be enabled.
 */
data class MediaStream(
    val id: String,
    val title: String,
    val url: String,
    val mimeType: String? = null,
    val quality: String? = null,
    val language: String? = null,
    val headers: Map<String, String> = emptyMap(),
    val subtitles: List<SubtitleTrack> = emptyList()
)

data class SubtitleTrack(
    val language: String,
    val url: String,
    val mimeType: String? = null
)

data class ProviderDescriptor(
    val id: String,
    val name: String,
    val version: String,
    val description: String,
    val enabledByDefault: Boolean = false
)

data class MediaRequest(
    val tmdbId: Int?,
    val imdbId: String?,
    val mediaType: MediaType,
    val season: Int? = null,
    val episode: Int? = null
)

enum class MediaType { MOVIE, TV, ANIME }
