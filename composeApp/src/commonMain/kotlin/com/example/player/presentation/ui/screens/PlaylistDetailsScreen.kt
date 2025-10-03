package com.example.player.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.player.presentation.mvi.playBack.PlaybackIntent
import com.example.player.presentation.mvi.playBack.PlaybackViewModel
import com.example.player.presentation.mvi.playBack.PlaybackViewState
import com.example.player.presentation.mvi.playListDetails.PlaylistDetailsViewModel
import com.example.player.presentation.mvi.playListDetails.PlaylistDetailsViewState
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.common.TrackListItem
import com.example.player.presentation.ui.layouts.GenericDetailScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlaylistDetailsScreen(
    viewModel: PlaylistDetailsViewModel,
    playbackViewModel: PlaybackViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val playbackState by playbackViewModel.viewState.collectAsState()

    PlaylistDetailsScreenContent(
        state = state,
        playbackState = playbackState,
        onPlaybackIntent = playbackViewModel::handleIntent,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
private fun PlaylistDetailsScreenContent(
    state: PlaylistDetailsViewState,
    playbackState: PlaybackViewState,
    onPlaybackIntent: (PlaybackIntent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GenericDetailScreen(
        title = state.playlist?.name ?: "Playlist",
        tracks = state.tracks,
        isLoading = state.isLoading,
        error = state.error,
        onBackClick = onBackClick,
        trackContent = { track ->
            TrackListItem(
                track = track,
                onClick = {
                    onPlaybackIntent(
                        PlaybackIntent.PlayTrackFromContext(
                            track,
                            state.tracks
                        )
                    )
                },
                isSelected = playbackState.playbackState.currentTrack?.id == track.id,
                isPlaying = playbackState.isPlaying,
                onFavoriteClick = { },
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun PlaylistDetailsScreenPreview() {
    val state = PlaylistDetailsViewState.sample
    val playbackState = PlaybackViewState.sample
    PlayerTheme {
        PlaylistDetailsScreenContent(
            state = state,
            playbackState = playbackState,
            onBackClick = {},
            onPlaybackIntent = {}
        )
    }
}