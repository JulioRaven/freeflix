package com.freeflix.app.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freeflix.app.BuildConfig
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

enum class MediaKind(val label: String, val endpoint: String) {
    MOVIE("Film", "movie"), TV("Série", "tv"), ANIME("Anime", "tv")
}

data class CatalogItem(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val kind: MediaKind
) {
    val year: String? get() = releaseDate?.take(4)?.takeIf { it.length == 4 }
    val posterUrl: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w342$it" }
    val backdropUrl: String? get() = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }
}

data class CatalogState(
    val items: List<CatalogItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val detailOverview: String? = null,
    val loadingDetail: Boolean = false
)

private data class TmdbPage(val results: List<TmdbItem> = emptyList())
private data class TmdbItem(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val release_date: String? = null,
    val first_air_date: String? = null
) {
    fun toCatalog(kind: MediaKind) = CatalogItem(
        id = id,
        title = title ?: name ?: "Sans titre",
        overview = overview.orEmpty(),
        posterPath = poster_path,
        backdropPath = backdrop_path,
        releaseDate = release_date ?: first_air_date,
        kind = kind
    )
}
private data class TmdbDetails(val overview: String? = null)

private interface TmdbApi {
    @GET("trending/movie/week") suspend fun trendingMovies(@Query("api_key") key: String, @Query("language") language: String = "fr-FR"): TmdbPage
    @GET("movie/popular") suspend fun popularMovies(@Query("api_key") key: String, @Query("language") language: String = "fr-FR"): TmdbPage
    @GET("tv/popular") suspend fun popularTv(@Query("api_key") key: String, @Query("language") language: String = "fr-FR"): TmdbPage
    @GET("search/multi") suspend fun search(@Query("api_key") key: String, @Query("query") query: String, @Query("language") language: String = "fr-FR"): TmdbPage
    @GET("{kind}/{id}") suspend fun details(@Path("kind") kind: String, @Path("id") id: Int, @Query("api_key") key: String, @Query("language") language: String = "fr-FR"): TmdbDetails
}

private object TmdbClient {
    val api: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }
}

class CatalogViewModel : ViewModel() {
    private val apiKey = BuildConfig.TMDB_API_KEY
    private val _state = kotlinx.coroutines.flow.MutableStateFlow(CatalogState())
    val state: kotlinx.coroutines.flow.StateFlow<CatalogState> = _state

    init { loadMovies() }

    fun loadMovies() = load {
        val page = if (apiKey.isBlank()) throw IllegalStateException("Clé TMDB absente. Configurez TMDB_API_KEY dans local.properties.") else TmdbClient.api.trendingMovies(apiKey)
        page.results.map { it.toCatalog(MediaKind.MOVIE) }
    }

    fun loadTv() = load {
        require(apiKey.isNotBlank()) { "Clé TMDB absente. Configurez TMDB_API_KEY dans local.properties." }
        TmdbClient.api.popularTv(apiKey).results.map { it.toCatalog(MediaKind.TV) }
    }

    fun search(query: String) {
        if (query.isBlank()) { loadMovies(); return }
        load {
            require(apiKey.isNotBlank()) { "Clé TMDB absente. Configurez TMDB_API_KEY dans local.properties." }
            TmdbClient.api.search(apiKey, query).results.mapNotNull {
                when {
                    it.title != null -> it.toCatalog(MediaKind.MOVIE)
                    it.name != null -> it.toCatalog(MediaKind.TV)
                    else -> null
                }
            }
        }
    }

    fun loadDetails(item: CatalogItem) {
        if (apiKey.isBlank()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(loadingDetail = true, detailOverview = null)
            runCatching { TmdbClient.api.details(item.kind.endpoint, item.id, apiKey).overview }
                .onSuccess { _state.value = _state.value.copy(detailOverview = it, loadingDetail = false) }
                .onFailure { _state.value = _state.value.copy(loadingDetail = false) }
        }
    }

    fun loadSourcesFor(item: CatalogItem) {
        // The source registry is connected separately from catalogue metadata.
        // Only providers with permission to serve the requested title should return streams.
        _state.value = _state.value.copy(error = "Aucune source n'est encore enregistrée dans cette version.")
    }

    private fun load(block: suspend () -> List<CatalogItem>) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            runCatching { block() }
                .onSuccess { _state.value = _state.value.copy(items = it, loading = false) }
                .onFailure { _state.value = _state.value.copy(loading = false, error = it.message ?: "Erreur de chargement.") }
        }
    }
}
