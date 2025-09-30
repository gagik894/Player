package com.example.player.domain.usecase

import com.example.player.domain.repository.PlayerRepository

class PlayPauseUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(isPlaying: Boolean) {
        if (isPlaying) {
            playerRepository.pause()
        } else {
            playerRepository.play()
        }
    }

    suspend fun play() {
        playerRepository.play()
    }

    suspend fun pause() {
        playerRepository.pause()
    }
}