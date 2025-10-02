package com.example.player.platform

/**
 * The 'actual' implementation for iOS.
 * Since we don't need any specific iOS context for the player,
 * this can be a simple empty class.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformContext

actual fun getPlatformContext(): PlatformContext = PlatformContext()