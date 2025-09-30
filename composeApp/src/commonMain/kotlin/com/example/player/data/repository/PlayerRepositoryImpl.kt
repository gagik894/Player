package com.example.player.data.repository

import com.example.player.domain.model.PlaybackState
import com.example.player.domain.model.RepeatMode
import com.example.player.domain.repository.PlayerRepository
import com.example.player.data.source.MockMusicDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Duration

class PlayerRepositoryImpl(
    mockDataSource: MockMusicDataSource
) : PlayerRepository {
    
    private val _playbackState = MutableStateFlow(
        PlaybackState(
            currentTrack = mockDataSource.getTracks().firstOrNull(),
            queue = mockDataSource.getTracks(),
            currentTrackIndex = 0
        )
    )
    
    override fun getPlaybackState(): Flow<PlaybackState> = _playbackState.asStateFlow()
    
    override suspend fun play() {
        _playbackState.value = _playbackState.value.copy(isPlaying = true)
    }
    
    override suspend fun pause() {
        _playbackState.value = _playbackState.value.copy(isPlaying = false)
    }
    
    override suspend fun stop() {
        _playbackState.value = _playbackState.value.copy(
            isPlaying = false,
            currentPosition = Duration.ZERO
        )
    }
    
    override suspend fun seekTo(position: Duration) {
        _playbackState.value = _playbackState.value.copy(currentPosition = position)
    }
    
    override suspend fun skipToNext() {
        val currentState = _playbackState.value
        val nextIndex = if (currentState.currentTrackIndex < currentState.queue.size - 1) {
            currentState.currentTrackIndex + 1
        } else {
            0 // Loop to beginning
        }
        
        _playbackState.value = currentState.copy(
            currentTrackIndex = nextIndex,
            currentTrack = currentState.queue.getOrNull(nextIndex),
            currentPosition = Duration.ZERO
        )
    }
    
    override suspend fun skipToPrevious() {
        val currentState = _playbackState.value
        val previousIndex = if (currentState.currentTrackIndex > 0) {
            currentState.currentTrackIndex - 1
        } else {
            currentState.queue.size - 1 // Loop to end
        }
        
        _playbackState.value = currentState.copy(
            currentTrackIndex = previousIndex,
            currentTrack = currentState.queue.getOrNull(previousIndex),
            currentPosition = Duration.ZERO
        )
    }
    
    override suspend fun toggleShuffle() {
        _playbackState.value = _playbackState.value.copy(
            isShuffleEnabled = !_playbackState.value.isShuffleEnabled
        )
    }
    
    override suspend fun setRepeatMode(mode: RepeatMode) {
        _playbackState.value = _playbackState.value.copy(repeatMode = mode)
    }
}