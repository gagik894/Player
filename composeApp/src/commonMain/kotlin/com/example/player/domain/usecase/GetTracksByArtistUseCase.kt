package com.example.player.domain.usecase

import com.example.player.domain.model.Track
import com.example.player.domain.repository.MusicRepository

/**
 * Use case for retrieving tracks by a specific artist
 */
class GetTracksByArtistUseCase(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(artistId: String): List<Track> {
        // Get all tracks and filter by artist ID
        return musicRepository.getInitialTracks().filter { track ->
            track.artist.id == artistId
        }
    }
}