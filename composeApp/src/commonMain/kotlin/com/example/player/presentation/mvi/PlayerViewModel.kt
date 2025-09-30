package com.example.player.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.data.source.MockMusicDataSource
import com.example.player.data.repository.MusicRepositoryImpl
import com.example.player.data.repository.PlayerRepositoryImpl
import com.example.player.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration

class PlayerViewModel : ViewModel() {

    private val mockDataSource = MockMusicDataSource()
    private val musicRepository = MusicRepositoryImpl(mockDataSource)
    private val playerRepository = PlayerRepositoryImpl(mockDataSource)
    
    // Use cases
    private val getPlaybackStateUseCase = GetPlaybackStateUseCase(playerRepository)
    private val playPauseUseCase = PlayPauseUseCase(playerRepository)
    private val skipTrackUseCase = SkipTrackUseCase(playerRepository)
    private val seekToPositionUseCase = SeekToPositionUseCase(playerRepository)
    private val toggleShuffleUseCase = ToggleShuffleUseCase(playerRepository)
    private val toggleRepeatModeUseCase = ToggleRepeatModeUseCase(playerRepository)
    private val toggleFavoriteUseCase = ToggleFavoriteUseCase(musicRepository)
    
    private val _viewState = MutableStateFlow(PlayerViewState())
    val viewState: StateFlow<PlayerViewState> = _viewState.asStateFlow()
    
    init {
        observePlaybackState()
    }
    
    fun handleIntent(intent: PlayerIntent) {
        when (intent) {
            is PlayerIntent.PlayPause -> handlePlayPause()
            is PlayerIntent.SkipNext -> handleSkipNext()
            is PlayerIntent.SkipPrevious -> handleSkipPrevious()
            is PlayerIntent.ToggleShuffle -> handleToggleShuffle()
            is PlayerIntent.ToggleRepeat -> handleToggleRepeat()
            is PlayerIntent.SeekTo -> handleSeekTo(intent.position)
            is PlayerIntent.ToggleFavorite -> handleToggleFavorite(intent.trackId)
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
                toggleRepeatModeUseCase(_viewState.value.repeatMode)
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