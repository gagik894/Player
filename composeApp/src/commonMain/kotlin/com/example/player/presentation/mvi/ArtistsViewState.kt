package com.example.player.presentation.mvi

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
)