package com.example.player.domain.usecase

import com.example.player.domain.model.Playlist
import com.example.player.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving all playlists
 */
class GetPlaylistsUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(): Flow<List<Playlist>> {
        return musicRepository.getAllPlaylists()
    }
}