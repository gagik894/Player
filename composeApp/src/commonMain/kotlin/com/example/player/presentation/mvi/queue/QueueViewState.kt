package com.example.player.presentation.mvi.queue

import com.example.player.domain.model.Track

data class QueueViewState(
    val queue: List<Track> = emptyList(),
    val currentTrack: Track? = null
)
