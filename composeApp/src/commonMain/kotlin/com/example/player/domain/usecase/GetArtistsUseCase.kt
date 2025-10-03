package com.example.player.domain.usecase

import com.example.player.domain.model.Artist
import com.example.player.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving all artists
 */
class GetArtistsUseCase(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(): Flow<List<Artist>> {
        return musicRepository.getAllArtists()
    }
}