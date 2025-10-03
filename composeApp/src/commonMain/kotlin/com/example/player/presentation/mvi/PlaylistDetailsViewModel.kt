package com.example.player.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.domain.model.Playlist
import com.example.player.domain.model.Track
import com.example.player.domain.usecase.GetPlaylistByIdUseCase
import com.example.player.domain.usecase.GetTracksByPlaylistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlaylistDetailsViewState(
    val playlist: Playlist? = null,
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class PlaylistDetailsViewModel(
    private val playlistId: String,
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
    private val getTracksByPlaylistUseCase: GetTracksByPlaylistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistDetailsViewState())
    val state: StateFlow<PlaylistDetailsViewState> = _state.asStateFlow()

    init {
        loadPlaylistAndTracks()
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