package com.example.player.presentation.mvi

import com.example.player.domain.model.PlaybackState
import com.example.player.presentation.util.toTimeString
import kotlin.time.Duration

/**
 * Represents the state of the playback view.
 *
 * @property isLoading Indicates whether the view is currently loading data.
 * @property error An optional error message to be displayed.
 * @property playbackState The current state of the media playback.
 * @property progress The current progress of the media playback, as a float between 0.0 and 1.0.
 * @property currentTime The current playback time, formatted as a string (e.g., "1:23").
 * @property totalTime The total duration of the media, formatted as a string (e.g., "3:45").
 * @property isPlaying A computed property that indicates whether media is currently playing.
 */
data class PlaybackViewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val playbackState: PlaybackState = PlaybackState(),
) {
    val isPlaying: Boolean get() = playbackState.isPlaying

    fun getDurationFromProgress(progress: Float): Duration {
        val currentTrack = playbackState.currentTrack
        return if (currentTrack != null) {
            Duration.parse("PT${(currentTrack.duration.inWholeSeconds * progress).toInt()}S")
        } else {
            Duration.ZERO
        }
    }

    val progress: Float
        get() {
            val currentTrack = playbackState.currentTrack
            return if (currentTrack != null && currentTrack.duration.inWholeSeconds > 0) {
                playbackState.currentPosition.inWholeSeconds.toFloat() / currentTrack.duration.inWholeSeconds.toFloat()
            } else {
                0f
            }
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

    val totalTime: String
        get() {
            val currentTrack = playbackState.currentTrack
            return currentTrack?.formattedDuration ?: "0:00"
        }

    companion object {
        val sample = PlaybackViewState(
            playbackState = PlaybackState.sample
        )
    }
}