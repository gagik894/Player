package com.example.player.domain.usecase

import com.example.player.domain.model.Artist
import com.example.player.domain.repository.MusicRepository

class GetArtistByIdUseCase(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(artistId: String): Artist? {
        return repository.getArtistById(artistId)
    }
}