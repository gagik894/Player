package com.example.player.presentation.mvi

import com.example.player.domain.model.PlaybackState
import kotlin.String

data class PlayerViewState(
    val playbackState: PlaybackState = PlaybackState(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val currentTrack = playbackState.currentTrack
    val isPlaying = playbackState.isPlaying
    val currentPosition = playbackState.currentPosition
    val repeatMode = playbackState.repeatMode

    val progress: Float
        get() = currentTrack?.duration?.let { totalDuration ->
            if (totalDuration.inWholeMilliseconds > 0) {
                (currentPosition.inWholeMilliseconds.toFloat() / totalDuration.inWholeMilliseconds.toFloat()).coerceIn(0f, 1f)
            } else 0f
        } ?: 0f
}
