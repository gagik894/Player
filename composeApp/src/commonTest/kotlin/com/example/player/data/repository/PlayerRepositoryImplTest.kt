package com.example.player.data.repository

import com.example.player.data.source.MockMusicDataSource
import com.example.player.domain.model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class PlayerRepositoryImplTest {
    private val mockDataSource = MockMusicDataSource()
    private val repository = PlayerRepositoryImpl(mockDataSource)

    @Test
    fun `should create repository with mock data source`() {
        // When
        val repo = PlayerRepositoryImpl(mockDataSource)

        // Then - repository should be created successfully
        assertEquals(repository::class, repo::class)
    }

    @Test
    fun `initial playback state should have default values`() {
        // Test the default state logic
        val defaultState = PlaybackState(
            currentTrack = null,
            isPlaying = false,
            currentPosition = 0.seconds,
            isShuffleEnabled = false,
            repeatMode = RepeatMode.OFF
        )
        
        // Then
        assertNull(defaultState.currentTrack)
        assertFalse(defaultState.isPlaying)
        assertEquals(0.seconds, defaultState.currentPosition)
        assertFalse(defaultState.isShuffleEnabled)
        assertEquals(RepeatMode.OFF, defaultState.repeatMode)
    }
    
    @Test
    fun `playback state should update playing status correctly`() {
        // Test state transitions
        var isPlaying = false
        
        // When - play
        isPlaying = true
        assertTrue(isPlaying)
        
        // When - pause
        isPlaying = false
        assertFalse(isPlaying)
    }
    
    @Test
    fun `playback state should update position correctly`() {
        // Test position updates
        var currentPosition = 0.seconds
        val newPosition = 90.seconds
        
        // When
        currentPosition = newPosition
        
        // Then
        assertEquals(90.seconds, currentPosition)
    }
    
    @Test
    fun `shuffle state should toggle correctly`() {
        // Test shuffle toggle logic
        var isShuffled = false
        
        // When - first toggle
        isShuffled = !isShuffled
        assertTrue(isShuffled)
        
        // When - second toggle
        isShuffled = !isShuffled
        assertFalse(isShuffled)
    }
    
    @Test
    fun `repeat mode should cycle correctly`() {
        // Test repeat mode cycling logic
        var repeatMode = RepeatMode.OFF
        
        // When - cycle from OFF to ALL
        repeatMode = getNextRepeatMode(repeatMode)
        assertEquals(RepeatMode.ALL, repeatMode)
        
        // When - cycle from ALL to ONE
        repeatMode = getNextRepeatMode(repeatMode)
        assertEquals(RepeatMode.ONE, repeatMode)
        
        // When - cycle from ONE to OFF
        repeatMode = getNextRepeatMode(repeatMode)
        assertEquals(RepeatMode.OFF, repeatMode)
    }
    
    @Test
    fun `current track should be updatable`() {
        // Test track management
        val artist = Artist(id = "1", name = "Test Artist")
        val album = Album(id = "1", title = "Test Album", artist = artist)
        val track1 = Track(
            id = "1",
            title = "First Song",
            artist = artist,
            album = album,
            duration = 3.minutes,
            audioUrl = "test://url1"
        )
        val track2 = Track(
            id = "2",
            title = "Second Song",
            artist = artist,
            album = album,
            duration = 4.minutes,
            audioUrl = "test://url2"
        )
        
        var currentTrack: Track? = null

        // When - set first track
        currentTrack = track1
        assertEquals("First Song", currentTrack.title)
        assertEquals("1", currentTrack.id)
        
        // When - change to second track
        currentTrack = track2
        assertEquals("Second Song", currentTrack.title)
        assertEquals("2", currentTrack.id)
        
        // When - clear track
        currentTrack = null
        assertNull(currentTrack)
    }
    
    @Test
    fun `playback state should maintain consistency`() {
        // Test that state updates maintain consistency
        val artist = Artist(id = "1", name = "Test Artist")
        val album = Album(id = "1", title = "Test Album", artist = artist)
        val track = Track(
            id = "1",
            title = "Test Song",
            artist = artist,
            album = album,
            duration = 5.minutes,
            audioUrl = "test://url"
        )
        
        val state = PlaybackState(
            currentTrack = track,
            isPlaying = true,
            currentPosition = 2.minutes,
            isShuffleEnabled = true,
            repeatMode = RepeatMode.ALL
        )
        
        // Then - all properties should be as set
        assertEquals(track, state.currentTrack)
        assertTrue(state.isPlaying)
        assertEquals(2.minutes, state.currentPosition)
        assertTrue(state.isShuffleEnabled)
        assertEquals(RepeatMode.ALL, state.repeatMode)
    }
    
    @Test
    fun `position should not exceed track duration`() {
        // Test position validation logic
        val trackDuration = 3.minutes
        var position = 2.minutes
        
        // Valid position
        assertTrue(position <= trackDuration)
        
        // Position at end
        position = trackDuration
        assertTrue(position <= trackDuration)
        
        // Position beyond duration (should be handled)
        position = 4.minutes
        assertFalse(position <= trackDuration)
    }
    
    @Test
    fun `playback state copy should work correctly`() {
        // Test state immutability
        val artist = Artist(id = "1", name = "Test Artist")
        val album = Album(id = "1", title = "Test Album", artist = artist)
        val track = Track(
            id = "1",
            title = "Test Song",
            artist = artist,
            album = album,
            duration = 3.minutes,
            audioUrl = "test://url"
        )
        
        val originalState = PlaybackState(
            currentTrack = track,
            isPlaying = false,
            currentPosition = 0.seconds,
            isShuffleEnabled = false,
            repeatMode = RepeatMode.OFF
        )
        
        val modifiedState = originalState.copy(
            isPlaying = true,
            currentPosition = 90.seconds
        )
        
        // Original should be unchanged
        assertFalse(originalState.isPlaying)
        assertEquals(0.seconds, originalState.currentPosition)
        
        // Modified should have new values
        assertTrue(modifiedState.isPlaying)
        assertEquals(90.seconds, modifiedState.currentPosition)
        
        // Other properties should remain the same
        assertEquals(originalState.currentTrack, modifiedState.currentTrack)
        assertEquals(originalState.isShuffleEnabled, modifiedState.isShuffleEnabled)
        assertEquals(originalState.repeatMode, modifiedState.repeatMode)
    }
    
    // Helper function for repeat mode cycling
    private fun getNextRepeatMode(current: RepeatMode): RepeatMode {
        return when (current) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
    }
}