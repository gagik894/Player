package com.example.player.domain.usecase

import com.example.player.domain.model.Track
import com.example.player.domain.repository.PlayerRepository

class SetQueueUseCase(private val playerRepository: PlayerRepository) {
    suspend operator fun invoke(tracks: List<Track>, startIndex: Int) {
        playerRepository.setQueue(tracks, startIndex)
    }
}