package com.example.player.domain.usecase

import com.example.player.domain.repository.PlayerRepository

class SkipTrackUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend fun skipToNext() {
        playerRepository.skipToNext()
    }
    
    suspend fun skipToPrevious() {
        playerRepository.skipToPrevious()
    }
}