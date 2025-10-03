package com.example.player.domain.usecase

import com.example.player.domain.model.Album
import com.example.player.domain.repository.MusicRepository

class GetAlbumsByArtistUseCase(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(artistId: String): List<Album> {
        return repository.getAlbumsByArtist(artistId)
    }
}