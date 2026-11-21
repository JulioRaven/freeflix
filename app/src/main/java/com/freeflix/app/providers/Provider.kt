package com.freeflix.app.providers

/**
 * Contract for a lawful provider adapter. Implementations may resolve direct,
 * authorized media URLs or user-owned library items; they must not bypass DRM,
 * subscriptions, authentication, or other access controls.
 */
interface MediaProvider {
    val descriptor: ProviderDescriptor
    suspend fun getStreams(request: MediaRequest): List<MediaStream>
}

class ProviderRegistry {
    private val providers = linkedMapOf<String, MediaProvider>()

    fun register(provider: MediaProvider) {
        providers[provider.descriptor.id] = provider
    }

    fun all(): List<MediaProvider> = providers.values.toList()

    fun enabled(): List<MediaProvider> =
        providers.values.filter { it.descriptor.enabledByDefault }

    fun find(id: String): MediaProvider? = providers[id]
}
