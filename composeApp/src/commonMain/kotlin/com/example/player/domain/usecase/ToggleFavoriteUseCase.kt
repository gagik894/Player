package com.example.player.domain.usecase

import com.example.player.domain.repository.MusicRepository

class ToggleFavoriteUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(trackId: String) {
        musicRepository.toggleFavorite(trackId)
    }
}