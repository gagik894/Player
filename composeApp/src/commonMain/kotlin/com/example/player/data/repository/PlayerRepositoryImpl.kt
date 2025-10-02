package com.example.player.data.repository

import com.example.player.domain.model.PlaybackState
import com.example.player.domain.model.RepeatMode
import com.example.player.domain.model.Track
import com.example.player.domain.repository.MusicRepository
import com.example.player.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Duration

class PlayerRepositoryImpl : PlayerRepository {
    private val _playbackState = MutableStateFlow(PlaybackState())
    override fun getPlaybackState(): Flow<PlaybackState> = _playbackState.asStateFlow()

    private var originalQueue: List<Track> = emptyList()

    override suspend fun setQueue(tracks: List<Track>, startIndex: Int) {
        originalQueue = tracks
        _playbackState.value = _playbackState.value.copy(
            queue = originalQueue,
            currentTrackIndex = startIndex,
            isPlaying = true, // Start playing immediately
            currentPosition = Duration.ZERO // Reset position
        )
    }

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
            currentPosition = Duration.ZERO
        )
    }

    override suspend fun skipToPrevious() {
        val currentState = _playbackState.value
        if (currentState.queue.isEmpty()) return

        val prevIndex = if (currentState.currentTrackIndex > 0) {
            currentState.currentTrackIndex - 1
        } else {
            currentState.queue.size - 1 // Loop to end
        }

        _playbackState.value = currentState.copy(
            currentTrackIndex = prevIndex,
            currentPosition = Duration.ZERO
        )
    }

    override suspend fun toggleShuffle() {
        val currentState = _playbackState.value
        val isShuffleEnabled = !currentState.isShuffleEnabled

        val newQueue = if (isShuffleEnabled) {
            originalQueue.shuffled()
        } else {
            originalQueue
        }

        _playbackState.value = currentState.copy(
            isShuffleEnabled = isShuffleEnabled,
            queue = newQueue,
            currentTrackIndex = if (newQueue.isNotEmpty()) 0 else -1
        )
    }

    override suspend fun setRepeatMode(mode: RepeatMode) {
        _playbackState.value = _playbackState.value.copy(repeatMode = mode)
    }
}