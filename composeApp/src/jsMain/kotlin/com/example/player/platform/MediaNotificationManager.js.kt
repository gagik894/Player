package com.example.player.platform

import com.example.player.domain.model.PlaybackState
import com.example.player.presentation.mvi.playback.PlaybackIntent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/** A No-Op implementation for WEB. */
private class IosMediaNotificationManager : MediaNotificationManager {
    override val events: Flow<PlaybackIntent> = emptyFlow()
    override fun showOrUpdate(state: PlaybackState) { /* Do nothing */ }
    override fun dismiss() { /* Do nothing */ }
}

actual fun createMediaNotificationManager(platformContext: PlatformContext): MediaNotificationManager {
    return IosMediaNotificationManager()
}