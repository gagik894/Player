package com.example.player.domain.model

import kotlin.time.Duration


/**
 * Represents a music track.
 *
 * @property id The unique identifier of the track.
 * @property title The title of the track.
 * @property artist The artist of the track.
 * @property album The album the track belongs to.
 * @property duration The duration of the track.
 * @property artworkUrl The URL of the track's artwork, if available.
 * @property audioUrl The URL of the track's audio.
 * @property isFavorite Whether the track is marked as a favorite.
 * @property trackNumber The track number within its album, if available.
 */
data class Track(
    val id: String,
    val title: String,
    val artist: Artist,
    val album: Album,
    val duration: Duration,
    val artworkUrl: String? = null,
    val audioUrl: String,
    val isFavorite: Boolean = false,
    val trackNumber: Int? = null
)