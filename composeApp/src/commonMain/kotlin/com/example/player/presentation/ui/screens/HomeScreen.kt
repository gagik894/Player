package com.example.player.presentation.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.player.navigation.PlayerDestination
import com.example.player.presentation.mvi.home.HomeIntent
import com.example.player.presentation.mvi.home.HomeViewModel
import com.example.player.presentation.mvi.home.HomeViewState
import com.example.player.presentation.mvi.playback.PlaybackIntent
import com.example.player.presentation.mvi.playback.PlaybackViewModel
import com.example.player.presentation.mvi.playback.PlaybackViewState
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.common.TrackListItem
import com.example.player.presentation.ui.components.homeScreen.FavoritesRow
import com.example.player.presentation.ui.layouts.GenericDetailScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    playbackViewModel: PlaybackViewModel,
    onNavigateTo: (PlayerDestination) -> Unit = {},
) {
    val homeViewState by homeViewModel.viewState.collectAsStateWithLifecycle()
    val playbackViewState by playbackViewModel.viewState.collectAsStateWithLifecycle()

    HomeContent(
        homeViewState = homeViewState,
        playbackViewState = playbackViewState,
        onHomeIntent = homeViewModel::handleIntent,
        onPlaybackIntent = playbackViewModel::handleIntent,
        onArtistsClick = {
            onNavigateTo(PlayerDestination.Artists)
        },
        onPlaylistsClick = {
            onNavigateTo(PlayerDestination.Playlists)
        },
        modifier = modifier
            .fillMaxSize()
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    homeViewState: HomeViewState,
    playbackViewState: PlaybackViewState,
    onHomeIntent: (HomeIntent) -> Unit,
    onPlaybackIntent: (PlaybackIntent) -> Unit,
    onArtistsClick: () -> Unit = {},
    onPlaylistsClick: () -> Unit = {}
) {
    val tracks = homeViewState.filteredTracks.ifEmpty { homeViewState.tracks }

    GenericDetailScreen(
        title = "Home",
        tracks = tracks,
        isLoading = homeViewState.isLoading,
        error = homeViewState.error,
        onBackClick = null,
        topBarActions = {
            IconButton(onClick = onArtistsClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Artists"
                )
            }
            IconButton(onClick = onPlaylistsClick) {
                Icon(
                    imageVector = Icons.Default.LibraryMusic,
                    contentDescription = "Playlists"
                )
            }
        },
        headerContent = {
            // Favorites section
            FavoritesRow(
                tracks = homeViewState.favorites,
                onTrackClick = { track ->
                    onPlaybackIntent(
                        PlaybackIntent.PlayTrackFromContext(
                            track,
                            homeViewState.favorites
                        )
                    )
                },
                modifier = Modifier.padding(bottom = 24.dp)
            )
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
                isSelected = playbackViewState.playbackState.currentTrack?.id == track.id,
                isPlaying = playbackViewState.playbackState.isPlaying,
                onClick = {
                    onPlaybackIntent(PlaybackIntent.PlayTrackFromContext(track, tracks))
                },
                onFavoriteClick = {
                    onHomeIntent(HomeIntent.ToggleFavorite(track.id))
                }
            )
        },
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    PlayerTheme {
        HomeContent(
            homeViewState = HomeViewState.sample,
            playbackViewState = PlaybackViewState.sample,
            onHomeIntent = {},
            onPlaybackIntent = {}
        )
    }
}
