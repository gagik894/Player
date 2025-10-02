package com.example.player.presentation.util

import kotlin.time.Duration

/**
 * Extension function to convert a Duration to a formatted time string (e.g., "1:23").
 *
 * @receiver Duration The duration to be formatted.
 * @return String The formatted time string.
 */
fun Duration.toTimeString(): String {
    val totalSeconds = this.inWholeSeconds
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes}:${if (seconds < 10) "0$seconds" else "$seconds"}"
}