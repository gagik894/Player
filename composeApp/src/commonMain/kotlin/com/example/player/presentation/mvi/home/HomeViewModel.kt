package com.example.player.presentation.mvi.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.di.RepositoryModule
import com.example.player.domain.model.Track
import com.example.player.domain.usecase.GetTracksUseCase
import com.example.player.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
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
        observeTracks()
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SearchTracks -> handleSearch(intent.query)
            is HomeIntent.ToggleFavorite -> handleToggleFavorite(intent.trackId)
            is HomeIntent.LoadTracks -> {  }
        }
    }

    private fun observeTracks() {
        viewModelScope.launch {
            getTracksUseCase()
                .catch { error ->
                    _viewState.update { it.copy(error = error.message, isLoading = false) }
                }
                .collect { allTracks ->
                    _viewState.update { currentState ->
                        currentState.copy(
                            tracks = allTracks,
                            favorites = allTracks.filter { track -> track.isFavorite },
                            filteredTracks = filterTracks(allTracks, currentState.searchQuery),
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun handleSearch(query: String) {
        _viewState.update { it.copy(searchQuery = query) }
    }

    private fun handleToggleFavorite(trackId: String) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(trackId)
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