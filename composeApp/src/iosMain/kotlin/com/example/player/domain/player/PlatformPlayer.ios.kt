package com.example.player.domain.player

import com.example.player.platform.PlatformContext
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemStatus
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
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.Foundation.addObserver
import platform.Foundation.removeObserver
import platform.darwin.NSObject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformPlayer actual constructor(platformContext: PlatformContext) {
    private var avPlayer: AVPlayer? = null
    private var playerItem: AVPlayerItem? = null
    private val _playerState = MutableStateFlow(PlayerState())
    actual val playerState = _playerState.asStateFlow()

    private var timeObserver: Any? = null
    private var statusObserver = StatusObserver { newStatus ->
        handlePlayerItemStatus(newStatus)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun prepare(url: String) {
        release()
        val nsUrl = NSURL.URLWithString(url) ?: run {
            _playerState.update { it.copy(error = "Invalid URL") }
            return
        }

        playerItem = AVPlayerItem(uRL = nsUrl)
        avPlayer = AVPlayer(playerItem = playerItem)

        playerItem?.addObserver(
            observer = statusObserver,
            forKeyPath = "status",
            options = NSKeyValueObservingOptionNew,
            context = null
        )

        val interval = CMTimeMakeWithSeconds(0.5, 1_000_000_000)
        timeObserver = avPlayer?.addPeriodicTimeObserverForInterval(interval, null) { time ->
            val position = CMTimeGetSeconds(time)
            _playerState.update { it.copy(currentPosition = position.seconds) }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun handlePlayerItemStatus(newStatus: AVPlayerItemStatus?) {
        when (newStatus) {
            AVPlayerItemStatusReadyToPlay -> {
                val duration = playerItem?.duration()?.let { CMTimeGetSeconds(it) } ?: 0.0
                _playerState.update { it.copy(totalDuration = duration.seconds, error = null) }
            }
            AVPlayerItemStatusFailed -> {
                _playerState.update { it.copy(error = "AVPlayer failed to load media.") }
            }
            AVPlayerItemStatusUnknown -> { /* Waiting for media to load */ }
            else -> { /* Do nothing */ }
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
        playerItem?.removeObserver(statusObserver, forKeyPath = "status")
        avPlayer = null
        playerItem = null
    }
}


/**
 * A helper class that inherits from NSObject to act as a Key-Value Observer.
 * This is the standard pattern for using KVO with Kotlin/Native.
 */
@OptIn(ExperimentalForeignApi::class)
private class StatusObserver(
    private val onStatusChanged: (AVPlayerItemStatus) -> Unit
) : NSObject() {
    override fun observeValueForKeyPath(
        keyPath: String?,
        ofObject: Any?,
        change: Map<Any?, *>?,
        context: COpaquePointer?
    ) {
        if (keyPath == "status") {
            val newStatusValue = (change?.get("new") as? NSNumber)?.longValue

            when (newStatusValue) {
                AVPlayerItemStatusReadyToPlay -> onStatusChanged(AVPlayerItemStatusReadyToPlay)
                AVPlayerItemStatusFailed -> onStatusChanged(AVPlayerItemStatusFailed)
                AVPlayerItemStatusUnknown -> onStatusChanged(AVPlayerItemStatusUnknown)
            }
        }
    }
}