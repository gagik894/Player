package com.example.player.presentation.mvi

import com.example.player.domain.model.Playlist

/**
 * Represents the state of the Playlists screen
 */
data class PlaylistsViewState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val playlists: List<Playlist> = emptyList(),
    val searchQuery: String = "",
    val filteredPlaylists: List<Playlist> = emptyList(),
    val selectedPlaylist: Playlist? = null
)