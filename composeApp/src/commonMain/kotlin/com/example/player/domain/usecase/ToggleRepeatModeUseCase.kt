package com.example.player.domain.usecase

import com.example.player.domain.model.RepeatMode
import com.example.player.domain.repository.PlayerRepository

class ToggleRepeatModeUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(currentMode: RepeatMode) {
        val nextMode = when (currentMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        playerRepository.setRepeatMode(nextMode)
    }
}