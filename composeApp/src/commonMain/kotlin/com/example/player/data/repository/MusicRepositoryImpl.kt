package com.example.player.data.repository

import com.example.player.domain.model.*
import com.example.player.domain.repository.MusicRepository
import com.example.player.data.source.MockMusicDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MusicRepositoryImpl(
    private val mockDataSource: MockMusicDataSource
) : MusicRepository {
    
    private val favoriteTrackIds = MutableStateFlow<Set<String>>(emptySet())
    
    override fun getAllTracks(): Flow<List<Track>> {
        return favoriteTrackIds.map { favorites ->
            mockDataSource.getTracks().map { track ->
                track.copy(isFavorite = track.id in favorites)
            }
        }
    }
    
    override fun getAllAlbums(): Flow<List<Album>> {
        return MutableStateFlow(mockDataSource.getAlbums())
    }
    
    override fun getAllArtists(): Flow<List<Artist>> {
        return MutableStateFlow(mockDataSource.getArtists())
    }
    
    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return MutableStateFlow(emptyList())
    }
    
    override suspend fun getTrackById(id: String): Track? {
        return mockDataSource.getTracks().find { it.id == id }?.let { track ->
            track.copy(isFavorite = track.id in favoriteTrackIds.value)
        }
    }
    
    override suspend fun getAlbumById(id: String): Album? {
        return mockDataSource.getAlbums().find { it.id == id }
    }
    
    override suspend fun getArtistById(id: String): Artist? {
        return mockDataSource.getArtists().find { it.id == id }
    }
    
    override suspend fun getPlaylistById(id: String): Playlist? {
        return null
    }
    
    override suspend fun getFavoriteTracks(): List<Track> {
        return mockDataSource.getTracks().filter { it.id in favoriteTrackIds.value }
    }
    
    override suspend fun toggleFavorite(trackId: String) {
        val currentFavorites = favoriteTrackIds.value.toMutableSet()
        if (trackId in currentFavorites) {
            currentFavorites.remove(trackId)
        } else {
            currentFavorites.add(trackId)
        }
        favoriteTrackIds.value = currentFavorites
    }
    
    override suspend fun searchTracks(query: String): List<Track> {
        return mockDataSource.getTracks().filter { 
            it.title.contains(query, ignoreCase = true) ||
            it.artist.name.contains(query, ignoreCase = true) ||
            it.album.title.contains(query, ignoreCase = true)
        }
    }
    
    override suspend fun searchAlbums(query: String): List<Album> {
        return mockDataSource.getAlbums().filter {
            it.title.contains(query, ignoreCase = true) ||
            it.artist.name.contains(query, ignoreCase = true)
        }
    }
    
    override suspend fun searchArtists(query: String): List<Artist> {
        return mockDataSource.getArtists().filter {
            it.name.contains(query, ignoreCase = true)
        }
    }
}