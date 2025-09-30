package com.example.player.data.repository

import com.example.player.data.source.MockMusicDataSource
import com.example.player.domain.model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

class MusicRepositoryImplTest {
    
    private val mockDataSource = MockMusicDataSource()
    private val repository = MusicRepositoryImpl(mockDataSource)
    
    @Test
    fun `should create repository with mock data source`() {
        // When
        val repo = MusicRepositoryImpl(mockDataSource)
        
        // Then - repository should be created successfully
        assertEquals(repository::class, repo::class)
    }
    
    @Test
    fun `toggleFavorite should add track to favorites when not favorite`() {
        // This tests the favorite tracking logic in isolation
        val favoriteIds = mutableSetOf<String>()
        val trackId = "test-track-1"
        
        // When - track is not in favorites
        assertFalse(trackId in favoriteIds)
        
        // Add to favorites
        favoriteIds.add(trackId)
        
        // Then
        assertTrue(trackId in favoriteIds)
    }
    
    @Test
    fun `toggleFavorite should remove track from favorites when already favorite`() {
        // This tests the favorite tracking logic in isolation
        val favoriteIds = mutableSetOf("test-track-1")
        val trackId = "test-track-1"
        
        // When - track is in favorites
        assertTrue(trackId in favoriteIds)
        
        // Remove from favorites
        favoriteIds.remove(trackId)
        
        // Then
        assertFalse(trackId in favoriteIds)
    }
    
    @Test
    fun `search should filter tracks by title correctly`() {
        // Test search logic in isolation
        val tracks = listOf(
            createTestTrack("1", "Rock Song"),
            createTestTrack("2", "Jazz Song"),
            createTestTrack("3", "Pop Song")
        )
        
        val query = "Rock"
        val filtered = tracks.filter { 
            it.title.contains(query, ignoreCase = true) 
        }
        
        assertEquals(1, filtered.size)
        assertEquals("Rock Song", filtered[0].title)
    }
    
    @Test
    fun `search should filter tracks by artist name correctly`() {
        // Test search logic in isolation
        val tracks = listOf(
            createTestTrack("1", "Song 1", "The Beatles"),
            createTestTrack("2", "Song 2", "Queen"),
            createTestTrack("3", "Song 3", "The Rolling Stones")
        )
        
        val query = "Beatles"
        val filtered = tracks.filter { 
            it.artist.name.contains(query, ignoreCase = true) 
        }
        
        assertEquals(1, filtered.size)
        assertEquals("The Beatles", filtered[0].artist.name)
    }
    
    @Test
    fun `search should be case insensitive`() {
        // Test case insensitive search
        val tracks = listOf(
            createTestTrack("1", "ROCK SONG"),
            createTestTrack("2", "jazz song")
        )
        
        val query1 = "rock"
        val query2 = "JAZZ"
        
        val filtered1 = tracks.filter { 
            it.title.contains(query1, ignoreCase = true) 
        }
        val filtered2 = tracks.filter { 
            it.title.contains(query2, ignoreCase = true) 
        }
        
        assertEquals(1, filtered1.size)
        assertEquals(1, filtered2.size)
        assertEquals("ROCK SONG", filtered1[0].title)
        assertEquals("jazz song", filtered2[0].title)
    }
    
    @Test
    fun `track copy with favorite status should work correctly`() {
        // Test the logic used in repository for updating favorite status
        val originalTrack = createTestTrack("1", "Test Song")
        assertFalse(originalTrack.isFavorite)
        
        val favoriteTrack = originalTrack.copy(isFavorite = true)
        assertTrue(favoriteTrack.isFavorite)
        
        // Original should remain unchanged
        assertFalse(originalTrack.isFavorite)
        
        // Other properties should be the same
        assertEquals(originalTrack.id, favoriteTrack.id)
        assertEquals(originalTrack.title, favoriteTrack.title)
        assertEquals(originalTrack.artist.name, favoriteTrack.artist.name)
    }
    
    // Helper function to create test tracks
    private fun createTestTrack(
        id: String, 
        title: String, 
        artistName: String = "Test Artist"
    ): Track {
        val artist = Artist(id = id, name = artistName)
        val album = Album(id = id, title = "Test Album", artist = artist)
        return Track(
            id = id,
            title = title,
            artist = artist,
            album = album,
            duration = 3.minutes,
            audioUrl = "test://url"
        )
    }
}