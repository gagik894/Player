package com.example.player.domain.player

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.player.platform.PlatformContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformPlayer actual constructor(platformContext: PlatformContext) {

    private val context = platformContext.androidContext
    private var exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _playerState = MutableStateFlow(PlayerState())
    actual val playerState = _playerState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var positionUpdateJob: Job? = null

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.update { it.copy(isPlaying = isPlaying) }
                if (isPlaying) startPositionUpdates() else stopPositionUpdates()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                _playerState.update { it.copy(isBuffering = playbackState == Player.STATE_BUFFERING) }
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                if (!playWhenReady) {
                    _playerState.update { it.copy(isPlaying = false) }
                    stopPositionUpdates()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                _playerState.update { it.copy(error = error.message ?: "Unknown ExoPlayer error") }
            }
        })
    }

    actual fun prepare(url: String) {
        exoPlayer.setMediaItem(MediaItem.fromUri(url))
        exoPlayer.prepare()
        scope.launch {
            while (exoPlayer.duration <= 0) { delay(100) }
            _playerState.update { it.copy(totalDuration = exoPlayer.duration.milliseconds) }
        }
    }

    actual fun play() {
        exoPlayer.play()
    }

    actual fun pause() {
        exoPlayer.pause()
    }

    actual fun stop() {
        exoPlayer.stop()
        _playerState.update { it.copy(currentPosition = Duration.ZERO) }
    }

    actual fun seekTo(position: Duration) {
        exoPlayer.seekTo(position.inWholeMilliseconds)
    }

    actual fun release() {
        stopPositionUpdates()
        scope.coroutineContext.cancelChildren()
        exoPlayer.release()
    }

    private fun startPositionUpdates() {
        stopPositionUpdates()
        positionUpdateJob = scope.launch {
            while (true) {
                _playerState.update {
                    it.copy(currentPosition = exoPlayer.currentPosition.milliseconds)
                }
                delay(250) // Update every 250ms - reasonable for data accuracy
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }
}