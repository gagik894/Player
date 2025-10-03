package com.example.player.domain.usecase

import com.example.player.domain.model.Track
import com.example.player.domain.repository.PlayerRepository

class PlayFromContextUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(trackId: String, tracks: List<Track>) {
        playerRepository.playFromContext(trackId, tracks)
    }
}