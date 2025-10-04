package com.example.player.playback

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.player.di.RepositoryModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        val playerRepository = RepositoryModule.playerRepository

        CoroutineScope(Dispatchers.Main).launch {
            when (action) {
                ACTION_PLAY_PAUSE -> {
                    val isPlaying = playerRepository.getPlaybackState().first().isPlaying
                    if (isPlaying) playerRepository.pause() else playerRepository.play()
                }
                ACTION_NEXT -> playerRepository.skipToNext()
                ACTION_PREVIOUS -> playerRepository.skipToPrevious()
            }
        }
    }

    companion object {
        const val ACTION_PLAY_PAUSE = "media.PLAY_PAUSE"
        const val ACTION_NEXT = "media.NEXT"
        const val ACTION_PREVIOUS = "media.PREVIOUS"
    }
}