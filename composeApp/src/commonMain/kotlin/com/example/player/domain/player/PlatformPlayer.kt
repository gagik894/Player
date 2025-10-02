package com.example.player.domain.player

import com.example.player.platform.PlatformContext
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

/**
 * A platform-agnostic data class representing the state of the media player.
 * This is the single source of truth for the low-level player's state.
 */
data class PlayerState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPosition: Duration = Duration.ZERO,
    val totalDuration: Duration = Duration.ZERO,
    val error: String? = null
)

/**
 * EXPECT DECLARATION
 *
 * Defines the common contract for a low-level media player. Each target platform
 * (Android, iOS, JS) must provide an 'actual' implementation of this class.
 */
expect class PlatformPlayer(platformContext: PlatformContext) {

    /**
     * A StateFlow that emits the current [PlayerState] of the player.
     * The PlayerRepository will observe this to get real-time updates.
     */
    val playerState: StateFlow<PlayerState>

    /**
     * Prepares the player to play media from a given URL.
     * This involves loading the media and buffering it.
     */
    fun prepare(url: String)

    /**
     * Starts or resumes playback.
     */
    fun play()

    /**
     * Pauses playback.
     */
    fun pause()

    /**
     * Stops playback and resets the player's position.
     */
    fun stop()

    /**
     * Seeks to a specific position in the media.
     */
    fun seekTo(position: Duration)

    /**
     * Releases all resources used by the player (e.g., memory, codecs).
     * This MUST be called when the player is no longer needed to prevent memory leaks.
     */
    fun release()
}