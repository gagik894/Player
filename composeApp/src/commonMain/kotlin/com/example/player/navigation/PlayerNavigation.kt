package com.example.player.navigation

import kotlinx.serialization.Serializable

/**
 * Navigation destinations for the Player app
 */
sealed interface PlayerDestination {
    @Serializable
    data object Home : PlayerDestination
    
    @Serializable
    data object Player : PlayerDestination
}