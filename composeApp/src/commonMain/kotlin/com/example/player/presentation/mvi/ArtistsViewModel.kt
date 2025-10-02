package com.example.player.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.di.RepositoryModule
import com.example.player.domain.model.Artist
import com.example.player.domain.usecase.GetArtistsUseCase
import com.example.player.domain.usecase.GetTracksByArtistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ArtistsIntent {
    data class SearchArtists(val query: String) : ArtistsIntent
    data class SelectArtist(val artist: Artist) : ArtistsIntent
    data object ClearSelection : ArtistsIntent
    data object LoadArtists : ArtistsIntent
}

class ArtistsViewModel : ViewModel() {

    // Shared repository
    private val musicRepository = RepositoryModule.musicRepository

    // Use cases
    private val getArtistsUseCase = GetArtistsUseCase(musicRepository)
    private val getTracksByArtistUseCase = GetTracksByArtistUseCase(musicRepository)

    // State
    private val _viewState = MutableStateFlow(ArtistsViewState())
    val viewState: StateFlow<ArtistsViewState> = _viewState.asStateFlow()
    
    init {
        observeArtists()
    }

    fun handleIntent(intent: ArtistsIntent) {
        when (intent) {
            is ArtistsIntent.SearchArtists -> handleSearch(intent.query)
            is ArtistsIntent.SelectArtist -> handleSelectArtist(intent.artist)
            is ArtistsIntent.ClearSelection -> handleClearSelection()
            is ArtistsIntent.LoadArtists -> { /* Already handled in init */ }
        }
    }

    private fun observeArtists() {
        viewModelScope.launch {
            getArtistsUseCase()
                .catch { error ->
                    _viewState.update { it.copy(error = error.message, isLoading = false) }
                }
                .collect { allArtists ->
                    _viewState.update { currentState ->
                        currentState.copy(
                            artists = allArtists,
                            filteredArtists = filterArtists(allArtists, currentState.searchQuery),
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun handleSearch(query: String) {
        _viewState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                filteredArtists = filterArtists(currentState.artists, query)
            )
        }
    }

    private fun handleSelectArtist(artist: Artist) {
        _viewState.update { it.copy(selectedArtist = artist) }
        
        viewModelScope.launch {
            try {
                val tracks = getTracksByArtistUseCase(artist.id)
                _viewState.update { it.copy(selectedArtistTracks = tracks) }
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }

    private fun handleClearSelection() {
        _viewState.update { 
            it.copy(
                selectedArtist = null, 
                selectedArtistTracks = emptyList()
            ) 
        }
    }
    
    private fun filterArtists(artists: List<Artist>, query: String): List<Artist> {
        return if (query.isBlank()) {
            artists
        } else {
            artists.filter { artist ->
                artist.name.contains(query, ignoreCase = true) ||
                artist.bio?.contains(query, ignoreCase = true) == true
            }
        }
    }
}