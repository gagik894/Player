package com.example.player.presentation.mvi.playListDetails

import com.example.player.domain.model.Playlist
import com.example.player.domain.model.Track

data class PlaylistDetailsViewState(
    val playlist: Playlist? = null,
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    companion object {
        val Empty = PlaylistDetailsViewState()
        val Loading = PlaylistDetailsViewState(isLoading = true)
        val Error = PlaylistDetailsViewState(error = "An error occurred")
        val sample = PlaylistDetailsViewState(
            playlist = Playlist.sample,
            tracks = listOf(
                Track.sample,
                Track.sample2,
                Track.sample3
            ),
            isLoading = false,
            error = null
        )
    }
}
