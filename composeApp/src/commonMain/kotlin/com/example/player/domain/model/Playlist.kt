package com.example.player.domain.model

/**
 * Represents a playlist of tracks.
 *
 * @property id The unique identifier of the playlist.
 * @property name The name of the playlist.
 * @property description An optional description for the playlist.
 * @property tracks A list of tracks included in the playlist. Defaults to an empty list.
 * @property coverUrl An optional URL for the playlist's cover image.
 * @property createdAt The timestamp (in milliseconds since epoch) when the playlist was created. Defaults to 0L.
 * @property updatedAt The timestamp (in milliseconds since epoch) when the playlist was last updated. Defaults to 0L.
 */
data class Playlist(
    val id: String,
    val name: String,
    val description: String? = null,
    val tracks: List<Track> = emptyList(),
    val coverUrl: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    companion object {
        val sample = Playlist(
            id = "playlist1",
            name = "My Favorite Songs",
            description = "A collection of my all-time favorite tracks.",
            tracks = listOf(Track.sample, Track.sample2, Track.sample3),
            coverUrl = "https://example.com/playlist_cover.jpg",
            createdAt = 1625155200000,
            updatedAt = 1625241600000
        )
    }
}