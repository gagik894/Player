package com.example.player.domain.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

data class PlaybackState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPosition: Duration = Duration.ZERO,
    val totalDuration: Duration = Duration.ZERO,
    val error: String? = null,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val queue: List<Track> = emptyList(),
    val currentTrackIndex: Int = -1
) {
    val currentTrack: Track?
        get() = if (currentTrackIndex in queue.indices) {
            queue[currentTrackIndex]
        } else null
        
    companion object {
        val sample: PlaybackState
            get() = PlaybackState(
                isPlaying = true,
                currentPosition = 1.minutes + 30.seconds,
                isShuffleEnabled = false,
                repeatMode = RepeatMode.ALL,
                queue = listOf(Track.sample, Track.sample2, Track.sample3),
                currentTrackIndex = 0
            )
    }
}

enum class RepeatMode {
    OFF, ALL, ONE
}