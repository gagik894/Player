package com.example.player.domain.repository

import com.example.player.domain.model.Track
import com.example.player.domain.model.Album
import com.example.player.domain.model.Artist
import com.example.player.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun getAllTracks(): Flow<List<Track>>
    fun getAllAlbums(): Flow<List<Album>>
    fun getAllArtists(): Flow<List<Artist>>
    fun getAllPlaylists(): Flow<List<Playlist>>
    
    suspend fun getTrackById(id: String): Track?
    suspend fun getAlbumById(id: String): Album?
    suspend fun getArtistById(id: String): Artist?
    suspend fun getPlaylistById(id: String): Playlist?
    
    suspend fun getFavoriteTracks(): List<Track>
    suspend fun toggleFavorite(trackId: String)
    
    suspend fun searchTracks(query: String): List<Track>
    suspend fun searchAlbums(query: String): List<Album>
    suspend fun searchArtists(query: String): List<Artist>
}