package com.example.player.domain.player

import com.example.player.platform.PlatformContext
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

actual class PlatformPlayer actual constructor(platformContext: PlatformContext) {
    actual val playerState: StateFlow<PlayerState>
        get() = TODO("Not yet implemented")

    actual fun prepare(url: String) {
    }

    actual fun play() {
    }

    actual fun pause() {
    }

    actual fun stop() {
    }

    actual fun seekTo(position: Duration) {
    }

    actual fun release() {
    }
}