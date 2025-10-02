package com.example.player.data.repository

import com.example.player.domain.model.PlaybackState
import com.example.player.domain.model.RepeatMode
import com.example.player.domain.model.Track
import com.example.player.domain.player.PlatformPlayer
import com.example.player.domain.repository.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class PlayerRepositoryImpl(
    private val platformPlayer: PlatformPlayer
) : PlayerRepository {

    // Backing state flow for playback state
    private val _playbackState = MutableStateFlow(PlaybackState())
    override fun getPlaybackState(): Flow<PlaybackState> = _playbackState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    // Original queue to restore order when shuffle is disabled
    private var originalQueue: List<Track> = emptyList()

    init {
        observePlatformPlayerState()
    }

    private fun observePlatformPlayerState() {
        scope.launch {
            platformPlayer.playerState.collect { playerState ->
                // Project the low-level state into our high-level application state.
                _playbackState.update {
                    it.copy(
                        isPlaying = playerState.isPlaying,
                        isBuffering = playerState.isBuffering,
                        currentPosition = playerState.currentPosition,
                        totalDuration = playerState.totalDuration,
                        error = playerState.error
                    )
                }

                // Handle automatic track transitions when a song finishes.
                val isFinished = !playerState.isPlaying && playerState.currentPosition > Duration.ZERO &&
                        playerState.currentPosition >= playerState.totalDuration

                if (isFinished) {
                    handleTrackFinished()
                }
            }
        }
    }

    override suspend fun setQueue(tracks: List<Track>, startIndex: Int) {
        originalQueue = tracks
        _playbackState.update {
            it.copy(
                queue = if (it.isShuffleEnabled) tracks.shuffled() else tracks,
                currentTrackIndex = startIndex
            )
        }
        playTrackAtIndex(startIndex)
    }

    override suspend fun play() {
        platformPlayer.play()
    }

    override suspend fun pause() {
        platformPlayer.pause()
    }

    override suspend fun stop() {
        platformPlayer.stop()
    }

    override suspend fun seekTo(position: Duration) {
        platformPlayer.seekTo(position)
    }

    override suspend fun skipToNext() {
        val currentState = _playbackState.value

        if (currentState.queue.isEmpty()) return

        val nextIndex = if (currentState.currentTrackIndex < currentState.queue.size - 1) {
            currentState.currentTrackIndex + 1
        } else {
            0 // Loop to beginning
        }

        playTrackAtIndex(nextIndex)
    }

    override suspend fun skipToPrevious() {
        val currentState = _playbackState.value
        if (currentState.queue.isEmpty()) return

        // If the track has been playing for more than 3 seconds, restart it.
        if (currentState.currentPosition > 3.seconds) {
            seekTo(Duration.ZERO)
            return
        }

        val prevIndex = if (currentState.currentTrackIndex > 0) {
            currentState.currentTrackIndex - 1
        } else {
            currentState.queue.size - 1 // Loop to the end
        }
        playTrackAtIndex(prevIndex)
    }

    override suspend fun toggleShuffle() {
        val currentState = _playbackState.value
        val isShuffleEnabled = !currentState.isShuffleEnabled
        val currentTrack = currentState.currentTrack

        val newQueue = if (isShuffleEnabled) {
            originalQueue.shuffled()
        } else {
            originalQueue // Restore original order
        }

        // After shuffling, find the new index of the track that was playing.
        val newIndex = if (currentTrack != null) newQueue.indexOf(currentTrack) else -1

        _playbackState.update {
            it.copy(
                isShuffleEnabled = isShuffleEnabled,
                queue = newQueue,
                currentTrackIndex = newIndex
            )
        }
    }

    override suspend fun setRepeatMode(mode: RepeatMode) {
        _playbackState.value = _playbackState.value.copy(repeatMode = mode)
    }

    /**
     * A private helper to handle the logic of playing a specific track from the queue.
     */
    private fun playTrackAtIndex(index: Int) {
        val state = _playbackState.value
        val trackToPlay = state.queue.getOrNull(index)

        if (trackToPlay != null) {
            platformPlayer.prepare(trackToPlay.audioUrl)
            platformPlayer.play()
            _playbackState.update { it.copy(currentTrackIndex = index) }
        } else {
            // If the index is invalid, stop playback.
            platformPlayer.stop()
            _playbackState.update { it.copy(isPlaying = false, currentTrackIndex = -1) }
        }
    }

    /**
     * A private helper to decide what to do when the current track finishes playing.
     */
    private suspend fun handleTrackFinished() {
        val currentState = _playbackState.value
        when (currentState.repeatMode) {
            RepeatMode.ONE -> {
                seekTo(Duration.ZERO)
                play()
            }
            RepeatMode.ALL -> {
                skipToNext()
            }
            RepeatMode.OFF -> {
                // If it's the last track in the queue, stop. Otherwise, play the next one.
                if (currentState.currentTrackIndex < currentState.queue.size - 1) {
                    skipToNext()
                } else {
                    stop()
                }
            }
        }
    }
}