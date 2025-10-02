package com.example.player.data.source

import com.example.player.domain.model.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MockMusicDataSource {
    
    private val sampleArtists = listOf(
        Artist(
            id = "1",
            name = "Queen",
            imageUrl = null
        ),
        Artist(
            id = "2", 
            name = "The Beatles",
            imageUrl = null
        ),
        Artist(
            id = "3",
            name = "Pink Floyd",
            imageUrl = null
        )
    )
    
    private val sampleAlbums = listOf(
        Album(
            id = "1",
            title = "A Night at the Opera",
            artist = sampleArtists[0],
            year = 1975,
            trackCount = 12
        ),
        Album(
            id = "2",
            title = "Abbey Road",
            artist = sampleArtists[1],
            year = 1969,
            trackCount = 17
        ),
        Album(
            id = "3",
            title = "The Dark Side of the Moon",
            artist = sampleArtists[2],
            year = 1973,
            trackCount = 10
        )
    )
    
    private val sampleTracks = listOf(
        Track(
            id = "1",
            title = "Bohemian Rhapsody",
            artist = sampleArtists[0],
            album = sampleAlbums[0],
            duration = 7.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827736_17313276-lq.mp3",
            trackNumber = 1
        ),
        Track(
            id = "2",
            title = "Love of My Life",
            artist = sampleArtists[0],
            album = sampleAlbums[0],
            duration = 55.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827647_13737430-lq.mp3",
            trackNumber = 2
        ),
        Track(
            id = "3",
            title = "Come Together",
            artist = sampleArtists[1],
            album = sampleAlbums[1],
            duration = 8.seconds,
            audioUrl = "https://cdn.freesound.org/previews/828/828106_71257-lq.mp3",
            trackNumber = 1
        ),
        Track(
            id = "4",
            title = "Something",
            artist = sampleArtists[1],
            album = sampleAlbums[1],
            duration = 2.minutes + 25.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827225_843915-lq.mp3",
            trackNumber = 2
        ),
        Track(
            id = "5",
            title = "Time",
            artist = sampleArtists[2],
            album = sampleAlbums[2],
            duration =7.seconds,
            audioUrl = "https://cdn.freesound.org/previews/828/828106_71257-lq.mp3",
            trackNumber = 1
        )
    )
    
    fun getTracks(): List<Track> = sampleTracks
    fun getAlbums(): List<Album> = sampleAlbums
    fun getArtists(): List<Artist> = sampleArtists
}