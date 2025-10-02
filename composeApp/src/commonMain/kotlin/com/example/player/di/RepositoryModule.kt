package com.example.player.di

import com.example.player.data.repository.MusicRepositoryImpl
import com.example.player.data.repository.PlayerRepositoryImpl
import com.example.player.data.source.MockMusicDataSource
import com.example.player.domain.player.PlatformPlayer
import com.example.player.domain.repository.MusicRepository
import com.example.player.domain.repository.PlayerRepository
import com.example.player.platform.getPlatformContext

/**
 * A simple, common singleton object for providing repository instances.
 *
 * This acts as a manual Dependency Injection container for the application.
 * By using the `getPlatformContext()` factory, this entire module can remain
 * in common code, free of any platform-specific logic.
 */
object RepositoryModule {

    /**
     * A singleton instance of the data source, shared between repositories if needed.
     * `by lazy` ensures it's only created once, when first accessed.
     */
    private val mockDataSource by lazy {
        MockMusicDataSource()
    }

    /**
     * Provides a singleton instance of the MusicRepository.
     */
    val musicRepository: MusicRepository by lazy {
        MusicRepositoryImpl(mockDataSource)
    }

    /**
     * Provides a singleton instance of the PlayerRepository.
     */
    val playerRepository: PlayerRepository by lazy {
        val platformContext = getPlatformContext()
        val platformPlayer = PlatformPlayer(platformContext)
        PlayerRepositoryImpl(platformPlayer)
    }
}