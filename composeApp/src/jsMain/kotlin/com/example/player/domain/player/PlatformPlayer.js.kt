package com.example.player.domain.player

import com.example.player.platform.PlatformContext
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.w3c.dom.HTMLAudioElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformPlayer actual constructor(platformContext: PlatformContext) {
    private val audio: HTMLAudioElement = document.createElement("audio") as HTMLAudioElement
    private val _playerState = MutableStateFlow(PlayerState())
    actual val playerState = _playerState.asStateFlow()
    
    private var positionTimer: Int? = null

    init {
        // Attach event listeners to the audio element to update our state.
        audio.onplay = { 
            _playerState.update { it.copy(isPlaying = true, isBuffering = false) }
            startPositionTimer()
        }
        audio.onpause = { 
            _playerState.update { it.copy(isPlaying = false) }
            stopPositionTimer()
        }
        audio.onwaiting = { _playerState.update { it.copy(isBuffering = true) } }
        audio.onplaying = { _playerState.update { it.copy(isBuffering = false) } }
        audio.ondurationchange = {
            if (!audio.duration.isNaN()) {
                _playerState.update { it.copy(totalDuration = audio.duration.seconds) }
            }
        }
        audio.ontimeupdate = {
            _playerState.update { it.copy(currentPosition = audio.currentTime.seconds) }
        }
        audio.onerror = { _, _, _, _, _ ->
            _playerState.update {
                it.copy(error = audio.error?.code?.toString() ?: "Unknown web audio error")
            }
            null
        }
    }

    private fun startPositionTimer() {
        stopPositionTimer()
        positionTimer = window.setInterval({
            if (!audio.paused && !audio.ended) {
                _playerState.update { it.copy(currentPosition = audio.currentTime.seconds) }
            }
        }, 250) // Update every 250ms for reasonable accuracy
    }

    private fun stopPositionTimer() {
        positionTimer?.let { window.clearInterval(it) }
        positionTimer = null
    }

    actual fun prepare(url: String) {
        audio.src = url
    }

    actual fun play() {
        audio.play().catch {
            _playerState.update { state ->
                state.copy(error = "Playback failed. User may need to interact with the page first.")
            }
        }
    }

    actual fun pause() {
        audio.pause()
    }

    actual fun stop() {
        pause()
        audio.currentTime = 0.0
    }

    actual fun seekTo(position: Duration) {
        audio.currentTime = position.inWholeSeconds.toDouble()
    }

    actual fun release() {
        pause()
        stopPositionTimer()
        audio.src = ""
    }
}