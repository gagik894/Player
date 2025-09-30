package com.example.player.domain.repository

import com.example.player.domain.model.PlaybackState
import com.example.player.domain.model.RepeatMode
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface PlayerRepository {
    fun getPlaybackState(): Flow<PlaybackState>
    suspend fun play()
    suspend fun pause()
    suspend fun stop()
    suspend fun seekTo(position: Duration)
    suspend fun skipToNext()
    suspend fun skipToPrevious()
    suspend fun toggleShuffle()
    suspend fun setRepeatMode(mode: RepeatMode)
}