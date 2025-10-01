package com.example.player.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.di.RepositoryModule
import com.example.player.domain.model.Track
import com.example.player.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration



sealed interface PlaybackIntent {
    data object PlayPause : PlaybackIntent
    data object SkipNext : PlaybackIntent
    data object SkipPrevious : PlaybackIntent
    data object ToggleShuffle : PlaybackIntent
    data object ToggleRepeat : PlaybackIntent
    data class SeekTo(val position: Duration) : PlaybackIntent
    data class PlayTrack(val track: Track) : PlaybackIntent
    data class ToggleFavorite(val trackId: String) : PlaybackIntent
}

class PlaybackViewModel : ViewModel() {

    // Shared repositories
    private val playerRepository = RepositoryModule.playerRepository
    private val musicRepository = RepositoryModule.musicRepository

    // Use cases
    private val getPlaybackStateUseCase = GetPlaybackStateUseCase(playerRepository)
    private val playPauseUseCase = PlayPauseUseCase(playerRepository)
    private val skipTrackUseCase = SkipTrackUseCase(playerRepository)
    private val seekToPositionUseCase = SeekToPositionUseCase(playerRepository)
    private val toggleShuffleUseCase = ToggleShuffleUseCase(playerRepository)
    private val toggleRepeatModeUseCase = ToggleRepeatModeUseCase(playerRepository)
    private val toggleFavoriteUseCase = ToggleFavoriteUseCase(musicRepository)

    // State
    private val _viewState = MutableStateFlow(PlaybackViewState())
    val viewState: StateFlow<PlaybackViewState> = _viewState.asStateFlow()
    
    init {
        observePlaybackState()
    }
    
    fun handleIntent(intent: PlaybackIntent) {
        when (intent) {
            is PlaybackIntent.PlayPause -> handlePlayPause()
            is PlaybackIntent.SkipNext -> handleSkipNext()
            is PlaybackIntent.SkipPrevious -> handleSkipPrevious()
            is PlaybackIntent.ToggleShuffle -> handleToggleShuffle()
            is PlaybackIntent.ToggleRepeat -> handleToggleRepeat()
            is PlaybackIntent.SeekTo -> handleSeekTo(intent.position)
            is PlaybackIntent.PlayTrack -> handlePlayTrack(intent.track)
            is PlaybackIntent.ToggleFavorite -> handleToggleFavorite(intent.trackId)
        }
    }
    
    private fun observePlaybackState() {
        viewModelScope.launch {
            getPlaybackStateUseCase()
                .catch { error ->
                    _viewState.update { 
                        it.copy(error = error.message, isLoading = false) 
                    }
                }
                .collect { playbackState ->
                    _viewState.update { 
                        it.copy(
                            playbackState = playbackState,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
    
    private fun handlePlayPause() {
        viewModelScope.launch {
            try {
                playPauseUseCase(_viewState.value.isPlaying)
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }
    
    private fun handleSkipNext() {
        viewModelScope.launch {
            try {
                skipTrackUseCase.skipToNext()
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }
    
    private fun handleSkipPrevious() {
        viewModelScope.launch {
            try {
                skipTrackUseCase.skipToPrevious()
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }
    
    private fun handleToggleShuffle() {
        viewModelScope.launch {
            try {
                toggleShuffleUseCase()
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }
    
    private fun handleToggleRepeat() {
        viewModelScope.launch {
            try {
                toggleRepeatModeUseCase(currentMode = _viewState.value.playbackState.repeatMode)
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }
    
    private fun handleSeekTo(position: Duration) {
        viewModelScope.launch {
            try {
                seekToPositionUseCase(position)
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }

    private fun handlePlayTrack(track: Track) {
        viewModelScope.launch {
            try {
                _viewState.update { currentState ->
                    currentState.copy(
                        playbackState = currentState.playbackState.copy(
                            currentTrack = track,
                            isPlaying = true
                        )
                    )
                }
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }

    private fun handleToggleFavorite(trackId: String) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(trackId)
                // Update the current track if it's the one being favorited
                _viewState.update { currentState ->
                    val currentTrack = currentState.playbackState.currentTrack
                    if (currentTrack?.id == trackId) {
                        currentState.copy(
                            playbackState = currentState.playbackState.copy(
                                currentTrack = currentTrack.copy(isFavorite = !currentTrack.isFavorite)
                            )
                        )
                    } else {
                        currentState
                    }
                }
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
    }
}