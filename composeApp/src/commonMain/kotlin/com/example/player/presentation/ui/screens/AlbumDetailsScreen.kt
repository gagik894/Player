package com.example.player.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.player.presentation.mvi.album.AlbumDetailsViewModel
import com.example.player.presentation.mvi.album.AlbumDetailsViewState
import com.example.player.presentation.mvi.playBack.PlaybackIntent
import com.example.player.presentation.mvi.playBack.PlaybackViewModel
import com.example.player.presentation.mvi.playBack.PlaybackViewState
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.common.TrackListItem
import com.example.player.presentation.ui.layouts.GenericDetailScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AlbumDetailsScreen(
    viewModel: AlbumDetailsViewModel,
    playbackViewModel: PlaybackViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val playbackState by playbackViewModel.viewState.collectAsState()

    AlbumDetailsContent(
        state = state,
        playbackState = playbackState,
        onPlaybackIntent = playbackViewModel::handleIntent,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
private fun AlbumDetailsContent(
    modifier: Modifier = Modifier,
    state: AlbumDetailsViewState,
    playbackState: PlaybackViewState,
    onPlaybackIntent: (PlaybackIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    GenericDetailScreen(
        title = state.album?.title ?: "Album",
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
fun AlbumDetailsScreenPreview() {
    val state = AlbumDetailsViewState.sample
    PlayerTheme {
        AlbumDetailsContent(
            state = state,
            playbackState = PlaybackViewState.sample,
            onPlaybackIntent = {},
            onBackClick = {}
        )
    }
}