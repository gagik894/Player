package com.example.player.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.player.presentation.mvi.ArtistDetailsViewModel
import com.example.player.presentation.mvi.PlaybackIntent
import com.example.player.presentation.mvi.PlaybackViewModel
import com.example.player.presentation.ui.layouts.GenericDetailScreen
import com.example.player.presentation.ui.components.common.TrackListItem
import com.example.player.presentation.theme.PlayerTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ArtistDetailsScreen(
    viewModel: ArtistDetailsViewModel,
    playbackViewModel: PlaybackViewModel,
    onBackClick: () -> Unit,
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