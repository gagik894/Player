package com.example.player.domain.usecase

import com.example.player.domain.repository.PlayerRepository

class ToggleShuffleUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke() {
        playerRepository.toggleShuffle()
    }
}