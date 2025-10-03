package com.example.player.domain.player

import com.example.player.platform.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemStatusFailed
import platform.AVFoundation.AVPlayerItemStatusReadyToPlay
import platform.AVFoundation.AVPlayerItemStatusUnknown
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.seekToTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSURL
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformPlayer actual constructor(platformContext: PlatformContext) {
    private var avPlayer: AVPlayer? = null
    private var playerItem: AVPlayerItem? = null
    private val _playerState = MutableStateFlow(PlayerState())
    actual val playerState = _playerState.asStateFlow()

    private var timeObserver: Any? = null
    private var statusCheckObserver: Any? = null

    @OptIn(ExperimentalForeignApi::class)
    actual fun prepare(url: String) {
        release()
        val nsUrl = NSURL.URLWithString(url) ?: run {
            _playerState.update { it.copy(error = "Invalid URL") }
            return
        }

        playerItem = AVPlayerItem(uRL = nsUrl)
        avPlayer = AVPlayer(playerItem = playerItem)

        // Poll the status periodically instead of using KVO
        val statusCheckInterval = CMTimeMakeWithSeconds(0.1, 1_000_000_000)
        statusCheckObserver =
            avPlayer?.addPeriodicTimeObserverForInterval(statusCheckInterval, null) { _ ->
                checkPlayerItemStatus()
            }

        val interval = CMTimeMakeWithSeconds(0.25, 1_000_000_000)
        timeObserver = avPlayer?.addPeriodicTimeObserverForInterval(interval, null) { time ->
            val position = CMTimeGetSeconds(time)
            _playerState.update { it.copy(currentPosition = position.seconds) }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun checkPlayerItemStatus() {
        playerItem?.let { item ->
            when (item.status) {
                AVPlayerItemStatusReadyToPlay -> {
                    val duration = item.duration().let { CMTimeGetSeconds(it) }
                    _playerState.update {
                        if (it.totalDuration == Duration.ZERO) {
                            // Only update once when first ready
                            statusCheckObserver?.let { observer ->
                                avPlayer?.removeTimeObserver(observer)
                                statusCheckObserver = null
                            }
                            it.copy(totalDuration = duration.seconds, error = null)
                        } else {
                            it
                        }
                    }
                }

                AVPlayerItemStatusFailed -> {
                    _playerState.update { it.copy(error = "AVPlayer failed to load media.") }
                    statusCheckObserver?.let { observer ->
                        avPlayer?.removeTimeObserver(observer)
                        statusCheckObserver = null
                    }
                }

                AVPlayerItemStatusUnknown -> { /* Still loading */
                }

                else -> {}
            }
        }
    }

    actual fun play() {
        avPlayer?.play()
        _playerState.update { it.copy(isPlaying = true) }
    }

    actual fun pause() {
        avPlayer?.pause()
        _playerState.update { it.copy(isPlaying = false) }
    }

    actual fun stop() {
        pause()
        seekTo(Duration.ZERO)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun seekTo(position: Duration) {
        val time = CMTimeMakeWithSeconds(position.inWholeSeconds.toDouble(), 1_000_000_000)
        avPlayer?.seekToTime(time)
    }

    actual fun release() {
        pause()
        timeObserver?.let { avPlayer?.removeTimeObserver(it) }
        timeObserver = null
        statusCheckObserver?.let { avPlayer?.removeTimeObserver(it) }
        statusCheckObserver = null
        avPlayer = null
        playerItem = null
    }
}