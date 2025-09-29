package com.example.player.domain.model

/**
 * Represents an artist.
 *
 * @property id The unique identifier of the artist.
 * @property name The name of the artist.
 * @property imageUrl The URL of the artist's image, if available.
 * @property bio A short biography or description of the artist, if available.
 */
data class Artist(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val bio: String? = null
)