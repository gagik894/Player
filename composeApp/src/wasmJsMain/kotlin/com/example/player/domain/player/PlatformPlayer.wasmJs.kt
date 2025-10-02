package com.example.player.domain.player

import com.example.player.platform.PlatformContext
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.w3c.dom.HTMLAudioElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalWasmJsInterop::class)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformPlayer actual constructor(platformContext: PlatformContext) {
    private val audio: HTMLAudioElement = document.createElement("audio") as HTMLAudioElement
    private val _playerState = MutableStateFlow(PlayerState())
    actual val playerState = _playerState.asStateFlow()

    private var positionTimer: Int? = null

    init {
        audio.onplay = { _ ->
            _playerState.update { it.copy(isPlaying = true, isBuffering = false) }
            startPositionTimer()
            null
        }
        audio.onpause = { _ ->
            _playerState.update { it.copy(isPlaying = false) }
            stopPositionTimer()
            null
        }
        audio.onwaiting = { _ ->
            _playerState.update { it.copy(isBuffering = true) }
            null
        }
        audio.onplaying = { _ ->
            _playerState.update { it.copy(isBuffering = false) }
            null
        }
        audio.ondurationchange = { _ ->
            if (!audio.duration.isNaN()) {
                _playerState.update { it.copy(totalDuration = audio.duration.seconds) }
            }
            null
        }
        audio.ontimeupdate = { _ ->
            _playerState.update { it.copy(currentPosition = audio.currentTime.seconds) }
            null
        }

        audio.onended = { _ ->
            _playerState.update { it.copy(isPlaying = false) }
            stopPositionTimer()
            null
        }

        audio.onerror = { _, _, _, _, _ ->
            _playerState.update {
                it.copy(error = audio.error?.code?.toString() ?: "Unknown web audio error")
            }
            stopPositionTimer()
            null
        }
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    private fun startPositionTimer() {
        stopPositionTimer()
        positionTimer = window.setInterval({
            _playerState.update { it.copy(currentPosition = audio.currentTime.seconds) }
            null
        }, 250) // Update position 4 times a second.
    }
    private fun stopPositionTimer() {
        positionTimer?.let { window.clearInterval(it) }
        positionTimer = null
    }

    actual fun prepare(url: String) {
        audio.src = url
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    actual fun play() {
        audio.play().catch {
            _playerState.update { state ->
                state.copy(error = "Playback failed. User may need to interact with the page first.")
            }
            // THE FIX IS HERE:
            null
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