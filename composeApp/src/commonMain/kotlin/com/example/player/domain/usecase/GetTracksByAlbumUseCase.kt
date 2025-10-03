package com.example.player.domain.usecase

import com.example.player.domain.repository.MusicRepository

class GetTracksByAlbumUseCase(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(albumId: String) = repository.getTracksByAlbum(albumId)
}