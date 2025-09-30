package com.example.player.domain.usecase

import com.example.player.domain.repository.PlayerRepository
import kotlin.time.Duration

class SeekToPositionUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(position: Duration) {
        playerRepository.seekTo(position)
    }
}