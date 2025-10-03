package com.example.player.presentation.mvi.playBack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.di.RepositoryModule
import com.example.player.domain.model.Track
import com.example.player.domain.usecase.GetPlaybackStateUseCase
import com.example.player.domain.usecase.PlayPauseUseCase
import com.example.player.domain.usecase.SeekToPositionUseCase
import com.example.player.domain.usecase.SetQueueUseCase
import com.example.player.domain.usecase.SkipTrackUseCase
import com.example.player.domain.usecase.ToggleFavoriteUseCase
import com.example.player.domain.usecase.ToggleRepeatModeUseCase
import com.example.player.domain.usecase.ToggleShuffleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration


sealed interface PlaybackIntent {
    data object PlayPause : PlaybackIntent
    data object SkipNext : PlaybackIntent
    data object SkipPrevious : PlaybackIntent
    data object ToggleShuffle : PlaybackIntent
    data object ToggleRepeat : PlaybackIntent
    data class SeekTo(val position: Duration) : PlaybackIntent
    data class PlayTrackFromContext(val track: Track, val context: List<Track>) : PlaybackIntent
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
    private val setQueueUseCase = SetQueueUseCase(playerRepository)

    // State
    private val _viewState = MutableStateFlow(PlaybackViewState())
    val viewState: StateFlow<PlaybackViewState> = _viewState.asStateFlow()
    
    init {
        observePlaybackState()
        observeMusicLibraryChanges()
    }
    
    fun handleIntent(intent: PlaybackIntent) {
        when (intent) {
            is PlaybackIntent.PlayPause -> handlePlayPause()
            is PlaybackIntent.SkipNext -> handleSkipNext()
            is PlaybackIntent.SkipPrevious -> handleSkipPrevious()
            is PlaybackIntent.ToggleShuffle -> handleToggleShuffle()
            is PlaybackIntent.ToggleRepeat -> handleToggleRepeat()
            is PlaybackIntent.SeekTo -> handleSeekTo(intent.position)
            is PlaybackIntent.PlayTrackFromContext -> handlePlayTrack(intent.track, intent.context)
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

    private fun observeMusicLibraryChanges() {
        viewModelScope.launch {
            musicRepository.getAllTracks().collect { allTracks ->
                _viewState.update { currentState ->
                    val currentQueue = currentState.playbackState.queue
                    if (currentQueue.isEmpty()) return@update currentState // Don't do anything if not playing

                    // Create an updated queue based on the latest data from the repository
                    val updatedQueue = currentQueue.map { trackInQueue ->
                        allTracks.find { it.id == trackInQueue.id } ?: trackInQueue
                    }

                    currentState.copy(
                        playbackState = currentState.playbackState.copy(queue = updatedQueue)
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

    private fun handlePlayTrack(track: Track, context: List<Track>) {
        viewModelScope.launch {
            try {
                val startIndex = context.indexOf(track).coerceAtLeast(0)
                setQueueUseCase(context, startIndex)
            } catch (e: Exception) {
                _viewState.update { it.copy(error = e.message) }
            }
        }
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
}