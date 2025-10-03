package com.example.player.domain.usecase

import com.example.player.domain.repository.MusicRepository

class GetAlbumsById(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(albumId: String) = repository.getAlbumById(albumId)
}