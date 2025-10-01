package com.example.player.di

import com.example.player.data.repository.MusicRepositoryImpl
import com.example.player.data.repository.PlayerRepositoryImpl
import com.example.player.data.source.MockMusicDataSource

/**
 * Simple dependency injection container for repositories
 */
object RepositoryModule {
    
    // Shared data source instance
    private val mockDataSource = MockMusicDataSource()
    
    // Shared repository instances
    val musicRepository = MusicRepositoryImpl(mockDataSource)
    val playerRepository = PlayerRepositoryImpl(mockDataSource)
}