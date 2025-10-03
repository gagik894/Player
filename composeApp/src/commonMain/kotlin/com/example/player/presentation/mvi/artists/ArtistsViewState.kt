package com.example.player.presentation.mvi.artists

import com.example.player.domain.model.Artist
import com.example.player.domain.model.Track

/**
 * Represents the state of the Artists screen
 */
data class ArtistsViewState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val artists: List<Artist> = emptyList(),
    val searchQuery: String = "",
    val filteredArtists: List<Artist> = emptyList(),
    val selectedArtist: Artist? = null,
    val selectedArtistTracks: List<Track> = emptyList()
) {
    companion object {
        val EMPTY = ArtistsViewState()
        val LOADING = ArtistsViewState(isLoading = true)
        val ERROR = ArtistsViewState(isLoading = false, error = "An error occurred")
        val sample = ArtistsViewState(
            isLoading = false,
            artists = listOf(
                Artist.Companion.sample,
                Artist.Companion.sample2
            ),
            filteredArtists = listOf(
                Artist.Companion.sample2
            )
        )
    }
}