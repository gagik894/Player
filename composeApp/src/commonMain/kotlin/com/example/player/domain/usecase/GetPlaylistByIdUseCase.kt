package com.example.player.domain.usecase

import com.example.player.domain.model.Playlist
import com.example.player.domain.repository.MusicRepository

class GetPlaylistByIdUseCase(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(playlistId: String): Playlist? {
        return repository.getPlaylistById(playlistId)
    }
}