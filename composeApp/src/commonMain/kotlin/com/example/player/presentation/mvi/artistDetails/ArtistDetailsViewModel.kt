package com.example.player.presentation.mvi.artistDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.domain.usecase.GetAlbumsByArtistUseCase
import com.example.player.domain.usecase.GetArtistByIdUseCase
import com.example.player.domain.usecase.GetTracksByArtistUseCase
import com.example.player.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtistDetailsViewModel(
    private val artistId: String,
    private val getArtistByIdUseCase: GetArtistByIdUseCase,
    private val getAlbumsByArtistUseCase: GetAlbumsByArtistUseCase,
    private val getTracksByArtistUseCase: GetTracksByArtistUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ArtistDetailsViewState())
    val state: StateFlow<ArtistDetailsViewState> = _state.asStateFlow()

    init {
        loadArtistData()
    }

    fun handleToggleFavorite(trackId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(trackId)
        }
    }

    private fun loadArtistData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val artist = getArtistByIdUseCase(artistId)
                val albums = getAlbumsByArtistUseCase(artistId)
                val tracks = getTracksByArtistUseCase(artistId)
                
                _state.value = _state.value.copy(
                    artist = artist,
                    albums = albums,
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