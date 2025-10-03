package com.example.player.presentation.mvi.artistDetails

import com.example.player.domain.model.Album
import com.example.player.domain.model.Artist
import com.example.player.domain.model.Track

data class ArtistDetailsViewState(
    val artist: Artist? = null,
    val albums: List<Album> = emptyList(),
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    companion object {
        val Empty = ArtistDetailsViewState()
        val Loading = ArtistDetailsViewState(isLoading = true)
        val Error = ArtistDetailsViewState(error = "An error occurred")
        val sample = ArtistDetailsViewState(
            artist = Artist.sample,
            albums = listOf(
                Album.sample,
                Album.sample2
            ),
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