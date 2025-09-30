package com.example.player.presentation.mvi

import com.example.player.domain.model.PlaybackState
import kotlin.String
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

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
                (currentPosition.inWholeMilliseconds.toFloat() / totalDuration.inWholeMilliseconds.toFloat()).coerceIn(
                    0f,
                    1f
                )
            } else 0f
        } ?: 0f

    val getDurationFromProgress: (Float) -> Duration = { progress ->
        currentTrack?.duration?.let { totalDuration ->
            (progress.coerceIn(0f, 1f) * totalDuration.inWholeMilliseconds).toLong().milliseconds
        } ?: Duration.ZERO
    }

    val currentTime: String
        get() = "${currentPosition.inWholeSeconds / 60}:${
            (currentPosition.inWholeSeconds % 60).toString().padStart(2, '0')
        }"

    val totalTime: String
        get() = currentTrack?.duration?.let { totalDuration ->
            "${totalDuration.inWholeSeconds / 60}:${
                (totalDuration.inWholeSeconds % 60).toString().padStart(2, '0')
            }"
        } ?: "0:00"

    companion object {
        val sample: PlayerViewState
            get() = PlayerViewState(
                playbackState = PlaybackState.sample,
                isLoading = false,
                error = null
            )
    }

}


