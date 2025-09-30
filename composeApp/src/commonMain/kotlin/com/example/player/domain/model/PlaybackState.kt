package com.example.player.domain.model

import kotlin.time.Duration

data class PlaybackState(
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Duration = Duration.ZERO,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val queue: List<Track> = emptyList(),
    val currentTrackIndex: Int = -1
)

enum class RepeatMode {
    OFF, ALL, ONE
}