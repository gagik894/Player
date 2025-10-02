package com.example.player.domain.model

/**
 * Represents the different repeat modes available for playback.
 * - OFF: No repeat, playback stops after the last track.
 * - ALL: Repeat the entire queue.
 * - ONE: Repeat the current track indefinitely.
 */
enum class RepeatMode {
    OFF, ALL, ONE
}