package com.example.player.domain.usecase

import com.example.player.domain.model.Track
import com.example.player.domain.repository.MusicRepository

class GetTracksByPlaylistUseCase(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(playlistId: String): List<Track> {
        return repository.getTracksByPlaylist(playlistId)
    }
}