package com.example.player.presentation.mvi.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.domain.usecase.GetAlbumsById
import com.example.player.domain.usecase.GetTracksByAlbumUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumDetailsViewModel(
    private val albumId: String,
    private val getAlbumByIdUseCase: GetAlbumsById,
    private val getTracksByAlbumUseCase: GetTracksByAlbumUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AlbumDetailsViewState())
    val state: StateFlow<AlbumDetailsViewState> = _state.asStateFlow()

    init {
        loadAlbumAndTracks()
    }

    private fun loadAlbumAndTracks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val album = getAlbumByIdUseCase(albumId)
                val tracks = getTracksByAlbumUseCase(albumId)

                _state.value = _state.value.copy(
                    album = album,
                    tracks = tracks,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}