package com.example.player.presentation.mvi.playLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.di.RepositoryModule
import com.example.player.domain.model.Playlist
import com.example.player.domain.usecase.GetPlaylistsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PlaylistsIntent {
    data class SearchPlaylists(val query: String) : PlaylistsIntent
    data class SelectPlaylist(val playlist: Playlist) : PlaylistsIntent
    data object ClearSelection : PlaylistsIntent
    data object LoadPlaylists : PlaylistsIntent
}

class PlaylistsViewModel : ViewModel() {

    // Shared repository
    private val musicRepository = RepositoryModule.musicRepository

    // Use cases
    private val getPlaylistsUseCase = GetPlaylistsUseCase(musicRepository)

    // State
    private val _viewState = MutableStateFlow(PlaylistsViewState())
    val viewState: StateFlow<PlaylistsViewState> = _viewState.asStateFlow()
    
    init {
        observePlaylists()
    }

    fun handleIntent(intent: PlaylistsIntent) {
        when (intent) {
            is PlaylistsIntent.SearchPlaylists -> handleSearch(intent.query)
            is PlaylistsIntent.SelectPlaylist -> handleSelectPlaylist(intent.playlist)
            is PlaylistsIntent.ClearSelection -> handleClearSelection()
            is PlaylistsIntent.LoadPlaylists -> { /* Already handled in init */ }
        }
    }

    private fun observePlaylists() {
        viewModelScope.launch {
            getPlaylistsUseCase()
                .catch { error ->
                    _viewState.update { it.copy(error = error.message, isLoading = false) }
                }
                .collect { allPlaylists ->
                    _viewState.update { currentState ->
                        currentState.copy(
                            playlists = allPlaylists,
                            filteredPlaylists = filterPlaylists(allPlaylists, currentState.searchQuery),
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
                filteredPlaylists = filterPlaylists(currentState.playlists, query)
            )
        }
    }

    private fun handleSelectPlaylist(playlist: Playlist) {
        _viewState.update { it.copy(selectedPlaylist = playlist) }
    }

    private fun handleClearSelection() {
        _viewState.update { it.copy(selectedPlaylist = null) }
    }
    
    private fun filterPlaylists(playlists: List<Playlist>, query: String): List<Playlist> {
        return if (query.isBlank()) {
            playlists
        } else {
            playlists.filter { playlist ->
                playlist.name.contains(query, ignoreCase = true) ||
                playlist.description?.contains(query, ignoreCase = true) == true
            }
        }
    }
}