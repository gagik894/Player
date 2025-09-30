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
){
    companion object {
        val sample: Artist
            get() = Artist(
                id = "1",
                name = "Queen",
                imageUrl = null,
                bio = "Queen are a British rock band formed in London in 1970. Their classic line-up was Freddie Mercury (lead vocals, piano), Brian May (guitar, vocals), Roger Taylor (drums, vocals) and John Deacon (bass)."
            )
    }
}