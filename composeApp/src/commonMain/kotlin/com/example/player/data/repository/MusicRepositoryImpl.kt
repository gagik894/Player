package com.example.player.data.repository

import com.example.player.data.source.MockMusicDataSource
import com.example.player.domain.model.Album
import com.example.player.domain.model.Artist
import com.example.player.domain.model.Playlist
import com.example.player.domain.model.Track
import com.example.player.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicRepositoryImpl(
    mockDataSource: MockMusicDataSource
) : MusicRepository {

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    private val tracks: Flow<List<Track>> = _tracks.asStateFlow()

    private val albums = mockDataSource.getAlbums()
    private val artists = mockDataSource.getArtists()
    private val playlists = mockDataSource.getPlaylists()

    init {
        _tracks.value = mockDataSource.getTracks()
    }

    override fun getAllTracks(): Flow<List<Track>> {
        return tracks
    }

    override fun getInitialTracks(): List<Track> {
        // Return the current value directly
        return _tracks.value
    }

    override fun getAllAlbums(): Flow<List<Album>> {
        return MutableStateFlow(albums)
    }

    override fun getAllArtists(): Flow<List<Artist>> {
        return MutableStateFlow(artists)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return MutableStateFlow(
            listOf(
            Playlist(
                id = "favorites",
                name = "Favorites",
                description = "Your favorite tracks",
                tracks = getFavoriteTracks()
            )
            ) + playlists
        )
    }

    override suspend fun getTrackById(id: String): Track? {
        return _tracks.value.find { it.id == id }
    }

    override suspend fun getAlbumById(id: String): Album? {
        return albums.find { it.id == id }
    }

    override suspend fun getArtistById(id: String): Artist? {
        return artists.find { it.id == id }
    }

    override suspend fun getPlaylistById(id: String): Playlist? {
        if (id == "favorites") {
            return Playlist(
                id = "favorites",
                name = "Favorites",
                description = "Your favorite tracks",
                tracks = getFavoriteTracks()
            )
        }
        return playlists.find { it.id == id }
    }

    override suspend fun getFavoriteTracks(): List<Track> {
        return _tracks.value.filter { it.isFavorite }
    }

    override suspend fun toggleFavorite(trackId: String) {
        val currentTracks = _tracks.value
        val updatedTracks = currentTracks.map { track ->
            if (track.id == trackId) {
                track.copy(isFavorite = !track.isFavorite)
            } else {
                track
            }
        }
        _tracks.value = updatedTracks
    }

    override suspend fun searchTracks(query: String): List<Track> {
        return _tracks.value.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.artist.name.contains(query, ignoreCase = true) ||
                    it.album.title.contains(query, ignoreCase = true)
        }
    }

    override suspend fun searchAlbums(query: String): List<Album> {
        return albums.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.artist.name.contains(query, ignoreCase = true)
        }
    }

    override suspend fun searchArtists(query: String): List<Artist> {
        return artists.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    override suspend fun searchPlaylists(query: String): List<Playlist> {
        return playlists.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description?.contains(query, ignoreCase = true) == true
        }
    }

    override suspend fun getTracksByArtist(artistId: String): List<Track> {
        return _tracks.value.filter { track ->
            track.artist.id == artistId
        }
    }

    override suspend fun getTracksByPlaylist(playlistId: String): List<Track> {
        val playlist = playlists.find { it.id == playlistId }
        return playlist?.tracks ?: emptyList()
    }

    override suspend fun getAlbumsByArtist(artistId: String): List<Album> {
        return albums.filter { album ->
            album.artist.id == artistId
        }
    }

    override suspend fun getTracksByAlbum(albumId: String): List<Track> {
        return _tracks.value.filter { track ->
            track.album.id == albumId
        }
    }
}