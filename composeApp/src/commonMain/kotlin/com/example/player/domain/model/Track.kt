package com.example.player.domain.model

import com.example.player.presentation.util.toTimeString
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
){
    val formattedDuration: String
        get() {
            return duration.toTimeString()
        }

    companion object {
        val sample: Track
            get() = Track(
                id = "1",
                title = "Bohemian Rhapsody",
                artist = Artist.sample,
                album = Album.sample,
                duration = Duration.parse("PT5M55S"),
                artworkUrl = null,
                audioUrl = "https://cdn.freesound.org/previews/828/828106_71257-lq.mp3",
                isFavorite = false,
                trackNumber = 1
            )
        val sample2: Track
            get() = Track(
                id = "2",
                title = "Don't Stop Me Now",
                artist = Artist.sample,
                album = Album.sample,
                duration = Duration.parse("PT3M29S"),
                artworkUrl = null,
                audioUrl = "https://cdn.freesound.org/previews/828/828106_71257-lq.mp3",
                isFavorite = true,
                trackNumber = 2
            )
        val sample3: Track
            get() = Track(
                id = "3",
                title = "Somebody to Love",
                artist = Artist.sample,
                album = Album.sample,
                duration = Duration.parse("PT4M56S"),
                artworkUrl = null,
                audioUrl = "https://cdn.freesound.org/previews/828/828106_71257-lq.mp3",
                isFavorite = false,
                trackNumber = 3
            )
    }
}