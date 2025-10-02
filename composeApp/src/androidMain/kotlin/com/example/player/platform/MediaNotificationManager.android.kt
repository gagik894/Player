package com.example.player.platform

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.player.domain.model.PlaybackState
import com.example.player.playback.PlaybackService
import com.example.player.presentation.mvi.PlaybackIntent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

private class AndroidMediaNotificationManager(private val context: Context) : MediaNotificationManager {
    override val events: Flow<PlaybackIntent> = emptyFlow()

    override fun showOrUpdate(state: PlaybackState) {
        val track = state.currentTrack
        if (track == null) {
            dismiss()
            return
        }

        val intent = Intent(context, PlaybackService::class.java).apply {
            putExtra(PlaybackService.EXTRA_TRACK_TITLE, track.title)
            putExtra(PlaybackService.EXTRA_ARTIST_NAME, track.artist.name)
            putExtra(PlaybackService.EXTRA_IS_PLAYING, state.isPlaying)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    override fun dismiss() {
        val intent = Intent(context, PlaybackService::class.java)
        context.stopService(intent)
    }
}

actual fun createMediaNotificationManager(platformContext: PlatformContext): MediaNotificationManager {
    return AndroidMediaNotificationManager(platformContext.androidContext)
}