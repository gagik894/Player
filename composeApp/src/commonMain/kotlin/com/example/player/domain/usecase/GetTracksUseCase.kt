package com.example.player.domain.usecase

import com.example.player.domain.model.Track
import com.example.player.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow

class GetTracksUseCase(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(): Flow<List<Track>> {
        return musicRepository.getAllTracks()
    }
}