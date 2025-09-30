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
){
    companion object {
        val sample: PlaybackState
            get() = PlaybackState(
                currentTrack = Track.sample,
                isPlaying = true,
                currentPosition = Duration.parse("PT1M30S"),
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