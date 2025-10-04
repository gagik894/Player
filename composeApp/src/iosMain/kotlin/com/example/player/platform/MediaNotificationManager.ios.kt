package com.example.player.platform

import com.example.player.domain.model.PlaybackState
import com.example.player.presentation.mvi.playback.PlaybackIntent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.MediaPlayer.MPChangePlaybackPositionCommandEvent
import platform.MediaPlayer.MPMediaItemPropertyAlbumTitle
import platform.MediaPlayer.MPMediaItemPropertyArtist
import platform.MediaPlayer.MPMediaItemPropertyPlaybackDuration
import platform.MediaPlayer.MPMediaItemPropertyTitle
import platform.MediaPlayer.MPNowPlayingInfoCenter
import platform.MediaPlayer.MPNowPlayingInfoPropertyElapsedPlaybackTime
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackRate
import platform.MediaPlayer.MPRemoteCommand
import platform.MediaPlayer.MPRemoteCommandCenter
import platform.MediaPlayer.MPRemoteCommandHandlerStatusSuccess
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


/**
 * The actual implementation for iOS that controls the Lock Screen and Control Center.
 */
private class IosMediaNotificationManager : MediaNotificationManager {

    override val events: Flow<PlaybackIntent> = callbackFlow {
        val commandCenter = MPRemoteCommandCenter.sharedCommandCenter()

        // Helper to add a handler and translate the result to our PlaybackIntent
        fun MPRemoteCommand.setHandler(intent: PlaybackIntent) {
            addTargetWithHandler { _ ->
                trySend(intent) // Send the event to our ViewModel
                MPRemoteCommandHandlerStatusSuccess
            }
        }

        // Register handlers for all the remote commands we want to support
        commandCenter.playCommand.setHandler(PlaybackIntent.PlayPause)
        commandCenter.pauseCommand.setHandler(PlaybackIntent.PlayPause)
        commandCenter.nextTrackCommand.setHandler(PlaybackIntent.SkipNext)
        commandCenter.previousTrackCommand.setHandler(PlaybackIntent.SkipPrevious)

        // You can also handle seek commands
        commandCenter.changePlaybackPositionCommand.addTargetWithHandler { event ->
            val positionEvent = event as MPChangePlaybackPositionCommandEvent
            val newPosition = positionEvent.positionTime.seconds
            trySend(PlaybackIntent.SeekTo(newPosition))
            MPRemoteCommandHandlerStatusSuccess
        }

        // This block is called when the Flow is cancelled.
        awaitClose {
            // Clean up all the handlers to prevent memory leaks.
            commandCenter.playCommand.removeTarget(null)
            commandCenter.pauseCommand.removeTarget(null)
            commandCenter.nextTrackCommand.removeTarget(null)
            commandCenter.previousTrackCommand.removeTarget(null)
            commandCenter.changePlaybackPositionCommand.removeTarget(null)
        }
    }

    override fun showOrUpdate(state: PlaybackState) {
        val track = state.currentTrack
        val nowPlayingInfoCenter = MPNowPlayingInfoCenter.defaultCenter()

        if (track == null) {
            // If nothing is playing, clear the info center.
            nowPlayingInfoCenter.nowPlayingInfo = null
            return
        }

        // Create a dictionary of metadata for the Now Playing screen.
        val nowPlayingInfo = mutableMapOf<String, Any>()
        nowPlayingInfo[MPMediaItemPropertyTitle] = track.title
        nowPlayingInfo[MPMediaItemPropertyArtist] = track.artist.name
        nowPlayingInfo[MPMediaItemPropertyAlbumTitle] = track.album.title
        nowPlayingInfo[MPMediaItemPropertyPlaybackDuration] =
            state.totalDuration.toDouble(DurationUnit.SECONDS)
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] =
            state.currentPosition.toDouble(DurationUnit.SECONDS)

        // Set the playback rate (1.0 for playing, 0.0 for paused)
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = if (state.isPlaying) 1.0 else 0.0

        nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo.toMap()
    }

    override fun dismiss() {
        MPNowPlayingInfoCenter.defaultCenter().nowPlayingInfo = null
    }
}

// This factory function now returns the real implementation.
actual fun createMediaNotificationManager(platformContext: PlatformContext): MediaNotificationManager {
    return IosMediaNotificationManager()
}