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
    
    @Serializable
    data object Artists : PlayerDestination
    
    @Serializable
    data object Playlists : PlayerDestination
    
    @Serializable
    data class ArtistDetail(val artistId: String) : PlayerDestination
    
    @Serializable
    data class PlaylistDetail(val playlistId: String) : PlayerDestination

    @Serializable
    data class AlbumDetail(val albumId: String) : PlayerDestination
}