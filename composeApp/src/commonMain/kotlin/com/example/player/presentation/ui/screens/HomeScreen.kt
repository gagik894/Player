package com.example.player.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.player.navigation.PlayerDestination
import com.example.player.presentation.mvi.HomeIntent
import com.example.player.presentation.mvi.HomeViewModel
import com.example.player.presentation.mvi.HomeViewState
import com.example.player.presentation.mvi.PlaybackIntent
import com.example.player.presentation.mvi.PlaybackViewModel
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.MiniPlayer
import com.example.player.presentation.ui.components.common.ErrorState
import com.example.player.presentation.ui.components.common.LoadingState
import com.example.player.presentation.ui.components.homeScreen.FavoritesRow
import com.example.player.presentation.ui.components.homeScreen.HomeTopBar
import com.example.player.presentation.ui.components.homeScreen.TrackListItem
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

    Scaffold(
        modifier = modifier,
        topBar = {
            HomeTopBar()
        },
        bottomBar = {
            Column {
                if (playbackViewState.playbackState.currentTrack != null){
                    MiniPlayer(
                        title = playbackViewState.playbackState.currentTrack?.title ?: "",
                        artistName = playbackViewState.playbackState.currentTrack?.artist?.name ?: "Unknown Artist",
                        isPlaying = playbackViewState.playbackState.isPlaying,
                        onPlayPauseClick = {
                            playbackViewModel.handleIntent(PlaybackIntent.PlayPause)
                        },
                        onNextClick = {
                            playbackViewModel.handleIntent(PlaybackIntent.SkipNext)
                        },
                        onExpandClick = {
                            onNavigateTo(PlayerDestination.Player)
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        when {
            homeViewState.isLoading -> LoadingState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )

            homeViewState.error != null -> ErrorState(
                error = homeViewState.error ?: "Unknown Error",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )

            else -> HomeContent(
                homeViewState = homeViewState,
                playbackViewState = playbackViewState,
                onHomeIntent = homeViewModel::handleIntent,
                onPlaybackIntent = playbackViewModel::handleIntent,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    homeViewState: HomeViewState,
    playbackViewState: com.example.player.presentation.mvi.PlaybackViewState,
    onHomeIntent: (HomeIntent) -> Unit,
    onPlaybackIntent: (PlaybackIntent) -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints {
            val isPortrait = maxHeight > maxWidth
            val tracks = homeViewState.filteredTracks.ifEmpty { homeViewState.tracks }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = if (isPortrait) 16.dp else 24.dp,
                    vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (
                    homeViewState.favorites.isNotEmpty()
                ) {
                    item {
                        Text(
                            text = "Liked Songs",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                horizontal = if (isPortrait) 4.dp else 8.dp,
                                vertical = 8.dp
                            )
                        )
                    }

                    item {
                        FavoritesRow(
                            tracks = homeViewState.favorites,
                            onTrackClick = { track ->
                                onPlaybackIntent(PlaybackIntent.PlayTrack(track))
                            }
                        )
                    }
                }
                item {
                    Text(
                        text = "All Songs",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = if (!isPortrait) 8.dp else 4.dp)
                    )
                }

                items(tracks) { track ->
                    TrackListItem(
                        track = track,
                        isCurrentlyPlaying = playbackViewState.playbackState.currentTrack?.id == track.id,
                        isPlaying = playbackViewState.playbackState.isPlaying,
                        onClick = {
                            onPlaybackIntent(PlaybackIntent.PlayTrack(track))
                        },
                        onFavoriteClick = {
                            onHomeIntent(HomeIntent.ToggleFavorite(track.id))
                        }
                    )
                }

                // Bottom padding for the mini player
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    PlayerTheme {
        HomeScreen(
            homeViewModel = remember { HomeViewModel() },
            playbackViewModel = remember { PlaybackViewModel() }
        )
    }
}
