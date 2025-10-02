package com.example.player.domain.player

/**
 * EXPECT DECLARATION for a factory function.
 *
 * Each platform must provide an 'actual' implementation that
 * creates and returns an instance of the platform-specific player.
 */
expect fun createPlatformPlayer(): PlatformPlayer