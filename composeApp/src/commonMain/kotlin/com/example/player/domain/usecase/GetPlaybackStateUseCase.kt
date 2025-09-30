package com.example.player.domain.usecase

import com.example.player.domain.model.PlaybackState
import com.example.player.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class GetPlaybackStateUseCase(
    private val playerRepository: PlayerRepository
) {
    operator fun invoke(): Flow<PlaybackState> {
        return playerRepository.getPlaybackState()
    }
}