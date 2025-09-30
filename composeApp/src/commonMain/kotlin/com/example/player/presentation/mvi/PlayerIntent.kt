package com.example.player.presentation.mvi

import kotlin.time.Duration

sealed interface PlayerIntent {
    data object PlayPause : PlayerIntent
    data object SkipNext : PlayerIntent
    data object SkipPrevious : PlayerIntent
    data object ToggleShuffle : PlayerIntent
    data object ToggleRepeat : PlayerIntent
    data class SeekTo(val position: Duration) : PlayerIntent
    data class ToggleFavorite(val trackId: String) : PlayerIntent
}