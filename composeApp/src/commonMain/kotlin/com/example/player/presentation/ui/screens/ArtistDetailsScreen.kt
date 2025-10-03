package com.example.player.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.player.domain.model.Album
import com.example.player.presentation.mvi.ArtistDetailsViewModel
import com.example.player.presentation.mvi.PlaybackIntent
import com.example.player.presentation.mvi.PlaybackViewModel
import com.example.player.presentation.ui.components.common.AlbumCard
import com.example.player.presentation.ui.layouts.GenericDetailScreen
import com.example.player.presentation.ui.components.common.TrackListItem
import com.example.player.presentation.theme.PlayerTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AlbumsRow(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit,
    modifier: Modifier = Modifier
) {
    if (albums.isNotEmpty()) {
        Column(modifier = modifier) {
            Text(
                text = "Albums",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(
                    items = albums,
                    key = { it.id }
                ) { album ->
                    AlbumCard(
                        album = album,
                        onClick = { onAlbumClick(album) }
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistDetailsScreen(
    viewModel: ArtistDetailsViewModel,
    playbackViewModel: PlaybackViewModel,
    onBackClick: () -> Unit,
    onAlbumClick: (Album) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val playbackState by playbackViewModel.viewState.collectAsState()
    
    GenericDetailScreen(
        title = state.artist?.name ?: "Artist",
        tracks = state.tracks,
        isLoading = state.isLoading,
        error = state.error,
        onBackClick = onBackClick,
        headerContent = {
            // Albums section
            AlbumsRow(
                albums = state.albums,
                onAlbumClick = onAlbumClick
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Tracks section title
            Text(
                text = "Songs",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
        },
        trackContent = { track ->
            TrackListItem(
                track = track,
                onClick = { playbackViewModel.handleIntent(PlaybackIntent.PlayTrackFromContext(track, state.tracks)) },
                isSelected = playbackState.playbackState.currentTrack?.id == track.id,
                isPlaying = playbackState.isPlaying,
                onFavoriteClick = { },
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ArtistDetailsScreenPreview() {
    PlayerTheme {
        // This would require a mock viewModel for preview
    }
}