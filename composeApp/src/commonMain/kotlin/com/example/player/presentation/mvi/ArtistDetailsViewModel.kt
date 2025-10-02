package com.example.player.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.domain.model.Artist
import com.example.player.domain.model.Track
import com.example.player.domain.usecase.GetArtistByIdUseCase
import com.example.player.domain.usecase.GetTracksByArtistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ArtistDetailsViewState(
    val artist: Artist? = null,
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ArtistDetailsViewModel(
    private val artistId: String,
    private val getArtistByIdUseCase: GetArtistByIdUseCase,
    private val getTracksByArtistUseCase: GetTracksByArtistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ArtistDetailsViewState())
    val state: StateFlow<ArtistDetailsViewState> = _state.asStateFlow()

    init {
        loadArtistAndTracks()
    }

    private fun loadArtistAndTracks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val artist = getArtistByIdUseCase(artistId)
                val tracks = getTracksByArtistUseCase(artistId)
                
                _state.value = _state.value.copy(
                    artist = artist,
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