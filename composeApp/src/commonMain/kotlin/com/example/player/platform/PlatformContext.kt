package com.example.player.platform

/**
 * An 'expect' declaration for a platform-specific context container.
 * This class will act as a wrapper for any platform-specific objects
 * that our common code needs, like the Android Context.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PlatformContext

/**
 * An 'expect' declaration for a factory function that provides
 * the current platform's context.
 */
expect fun getPlatformContext(): PlatformContext