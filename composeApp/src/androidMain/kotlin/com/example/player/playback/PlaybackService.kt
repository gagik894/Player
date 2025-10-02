package com.example.player.playback

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.player.MainActivity
import com.example.player.R

class PlaybackService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val trackTitle = intent?.getStringExtra(EXTRA_TRACK_TITLE) ?: "Unknown Title"
        val artistName = intent?.getStringExtra(EXTRA_ARTIST_NAME) ?: "Unknown Artist"
        val isPlaying = intent?.getBooleanExtra(EXTRA_IS_PLAYING, false) ?: false

        createNotificationChannel()
        val notification = createNotification(trackTitle, artistName, isPlaying)
        startForeground(NOTIFICATION_ID, notification)

        return START_NOT_STICKY
    }

    private fun createNotification(title: String, artist: String, isPlaying: Boolean): Notification {
        val contentIntent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(this, 0, contentIntent, PendingIntent.FLAG_IMMUTABLE)

        //TODO: UPDATE ICONS

        val playPauseIcon = if (isPlaying) R.drawable.ic_launcher_background else R.drawable.ic_launcher_background // You need these drawables
        val playPauseIntent = createActionIntent(NotificationActionReceiver.ACTION_PLAY_PAUSE)
        val nextIntent = createActionIntent(NotificationActionReceiver.ACTION_NEXT)
        val prevIntent = createActionIntent(NotificationActionReceiver.ACTION_PREVIOUS)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(artist)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(contentPendingIntent)
            .addAction(R.drawable.ic_launcher_background, "Previous", prevIntent)
            .addAction(playPauseIcon, "Play/Pause", playPauseIntent)
            .addAction(R.drawable.ic_launcher_background, "Next", nextIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2))
            .setOngoing(true)
            .build()
    }

    private fun createActionIntent(action: String): PendingIntent {
        val intent = Intent(this, NotificationActionReceiver::class.java).setAction(action)
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Media Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "PlaybackServiceChannel"
        const val NOTIFICATION_ID = 1
        const val EXTRA_TRACK_TITLE = "EXTRA_TRACK_TITLE"
        const val EXTRA_ARTIST_NAME = "EXTRA_ARTIST_NAME"
        const val EXTRA_IS_PLAYING = "EXTRA_IS_PLAYING"
    }
}