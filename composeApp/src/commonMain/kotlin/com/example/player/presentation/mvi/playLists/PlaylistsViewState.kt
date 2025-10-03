package com.example.player.presentation.mvi.playLists

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
) {
    companion object {
        val Empty = PlaylistsViewState()
        val Loading = PlaylistsViewState(isLoading = true)
        val Error = PlaylistsViewState(isLoading = false, error = "An error occurred")
        val sample = PlaylistsViewState(
            isLoading = false,
            playlists = List(10) { index ->
                Playlist.Companion.sample
            },
            filteredPlaylists = List(5) { index ->
                Playlist.Companion.sample
            }
        )
    }
}