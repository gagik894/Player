package com.example.player.presentation.mvi.playListDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.domain.usecase.GetPlaylistByIdUseCase
import com.example.player.domain.usecase.GetTracksByPlaylistUseCase
import com.example.player.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistId: String,
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
    private val getTracksByPlaylistUseCase: GetTracksByPlaylistUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistDetailsViewState())
    val state: StateFlow<PlaylistDetailsViewState> = _state.asStateFlow()

    init {
        loadPlaylistAndTracks()
    }

    fun handleToggleFavorite(trackId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(trackId)
        }
    }

    private fun loadPlaylistAndTracks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val playlist = getPlaylistByIdUseCase(playlistId)
                val tracks = getTracksByPlaylistUseCase(playlistId)
                
                _state.value = _state.value.copy(
                    playlist = playlist,
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