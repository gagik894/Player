package com.example.player.presentation.mvi.playback

import com.example.player.domain.model.PlaybackState
import com.example.player.presentation.util.toTimeString
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Represents the state of the playback view.
 *
 * @property isLoading Indicates whether the view is currently loading data.
 * @property error An optional error message to be displayed.
 * @property playbackState The current state of the media playback.
 * @property progress The current progress of the media playback, as a float between 0.0 and 1.0.
 * @property currentTime The current playback time, formatted as a string (e.g., "1:23").
 * @property isPlaying A computed property that indicates whether media is currently playing.
 */
data class PlaybackViewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val playbackState: PlaybackState = PlaybackState(),
) {
    val isPlaying: Boolean get() = playbackState.isPlaying

    fun getDurationFromProgress(progress: Float): Duration {
        val totalDurationMs = playbackState.totalDuration.inWholeMilliseconds
        if (totalDurationMs <= 0) return Duration.ZERO

        val newPositionMs = (totalDurationMs * progress.coerceIn(0f, 1f)).toLong()
        return newPositionMs.milliseconds
    }

    val progress: Float
        get() {
            val totalDurationMs = playbackState.totalDuration.inWholeMilliseconds
            if (totalDurationMs <= 0) return 0f

            val currentPositionMs = playbackState.currentPosition.inWholeMilliseconds
            return (currentPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
        }

    val currentTime: String
        get() {
            val currentTrack = playbackState.currentTrack
            return if (currentTrack != null) {
                 playbackState.currentPosition.toTimeString()
            } else {
                "0:00"
            }
        }

    companion object {
        val sample = PlaybackViewState(
            playbackState = PlaybackState.sample,
        )
    }
}