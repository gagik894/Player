package com.example.player.domain.model

/**
 * Represents an album in the music player.
 *
 * @property id The unique identifier of the album.
 * @property title The title of the album.
 * @property artist The artist of the album.
 * @property year The year the album was released.
 * @property artworkUrl The URL of the album artwork.
 * @property trackCount The number of tracks in the album.
 */
data class Album(
    val id: String,
    val title: String,
    val artist: Artist,
    val year: Int? = null,
    val artworkUrl: String? = null,
    val trackCount: Int = 0
){
    companion object {
        val sample: Album
            get() = Album(
                id = "1",
                title = "A Night at the Opera",
                artist = Artist.sample,
                year = 1975,
                artworkUrl = null,
                trackCount = 12
            )
        val sample2: Album
            get() = Album(
                id = "2",
                title = "The Dark Side of the Moon",
                artist = Artist.sample2,
                year = 1973,
                artworkUrl = null,
                trackCount = 10
            )
    }
}