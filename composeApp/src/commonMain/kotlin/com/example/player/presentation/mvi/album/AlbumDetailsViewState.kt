package com.example.player.presentation.mvi.album

import com.example.player.domain.model.Album
import com.example.player.domain.model.Track

data class AlbumDetailsViewState(
    val album: Album? = null,
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    companion object {
        val Empty = AlbumDetailsViewState()
        val Loading = AlbumDetailsViewState(isLoading = true)
        val Error = AlbumDetailsViewState(error = "An error occurred")
        val sample = AlbumDetailsViewState(
            album = Album.sample,
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
