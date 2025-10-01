package com.example.player.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.di.RepositoryModule
import com.example.player.domain.model.Track
import com.example.player.domain.usecase.GetTracksUseCase
import com.example.player.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch



sealed interface HomeIntent {
    data class SearchTracks(val query: String) : HomeIntent
    data class ToggleFavorite(val trackId: String) : HomeIntent
    data object LoadTracks : HomeIntent
}

class HomeViewModel : ViewModel() {

    // Shared repository
    private val musicRepository = RepositoryModule.musicRepository

    // Use cases
    private val getTracksUseCase = GetTracksUseCase(musicRepository)
    private val toggleFavoriteUseCase = ToggleFavoriteUseCase(musicRepository)

    // State
    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState: StateFlow<HomeViewState> = _viewState.asStateFlow()
    
    init {
        loadTracks()
    }
    
    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SearchTracks -> handleSearch(intent.query)
            is HomeIntent.ToggleFavorite -> handleToggleFavorite(intent.trackId)
            is HomeIntent.LoadTracks -> loadTracks()
        }
    }
    
    private fun loadTracks() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            try {
                getTracksUseCase()
                    .catch { error ->
                        _viewState.update { 
                            it.copy(error = error.message, isLoading = false) 
                        }
                    }
                    .collect { tracks ->
                        _viewState.update {
                            it.copy(
                                tracks = tracks,
                                favorites = tracks.filter { track ->  track.isFavorite },
                                filteredTracks = filterTracks(tracks, it.searchQuery),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                _viewState.update { 
                    it.copy(error = e.message, isLoading = false) 
                }
            }
        }
    }
    
    private fun handleSearch(query: String) {
        _viewState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                filteredTracks = filterTracks(currentState.tracks, query)
            )
        }
    }
    
    private fun handleToggleFavorite(trackId: String) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(trackId)
                
                _viewState.update { currentState ->
                    val updatedTracks = currentState.tracks.map { track ->
                        if (track.id == trackId) {
                            track.copy(isFavorite = !track.isFavorite)
                        } else {
                            track
                        }
                    }
                    
                    val updatedFavorites = updatedTracks.filter { it.isFavorite }
                    val updatedFilteredTracks = filterTracks(updatedTracks, currentState.searchQuery)

                    currentState.copy(
                        tracks = updatedTracks,
                        favorites = updatedFavorites,
                        filteredTracks = updatedFilteredTracks
                    )
                }
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }
    
    private fun filterTracks(tracks: List<Track>, query: String): List<Track> {
        return if (query.isBlank()) {
            tracks
        } else {
            tracks.filter { track ->
                track.title.contains(query, ignoreCase = true) ||
                track.artist.name.contains(query, ignoreCase = true) ||
                track.album.title.contains(query, ignoreCase = true)
            }
        }
    }
}