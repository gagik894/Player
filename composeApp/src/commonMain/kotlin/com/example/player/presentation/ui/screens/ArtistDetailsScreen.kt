package com.example.player.presentation.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.player.domain.model.Album
import com.example.player.presentation.mvi.artistDetails.ArtistDetailsViewModel
import com.example.player.presentation.mvi.artistDetails.ArtistDetailsViewState
import com.example.player.presentation.mvi.playback.PlaybackIntent
import com.example.player.presentation.mvi.playback.PlaybackViewModel
import com.example.player.presentation.mvi.playback.PlaybackViewState
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.artistDetailsScreen.AlbumsRow
import com.example.player.presentation.ui.components.common.TrackListItem
import com.example.player.presentation.ui.layouts.GenericDetailScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


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

    ArtistDetailsContent(
        state = state,
        playbackState = playbackState,
        onPlaybackIntent = playbackViewModel::handleIntent,
        onBackClick = onBackClick,
        onAlbumClick = onAlbumClick,
        modifier = modifier
    )
}

@Composable
private fun ArtistDetailsContent(
    modifier: Modifier = Modifier,
    state: ArtistDetailsViewState,
    playbackState: PlaybackViewState,
    onPlaybackIntent: (PlaybackIntent) -> Unit,
    onBackClick: () -> Unit,
    onAlbumClick: (Album) -> Unit = {},
) {
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
                onAlbumClick = onAlbumClick,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            // Tracks section title
            Text(
                text = "Songs",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(12.dp))
        },
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
private fun ArtistDetailsScreenPreview() {
    val sampleArtistDetailsViewState = ArtistDetailsViewState.sample
    val samplePlaybackViewState = PlaybackViewState.sample
    PlayerTheme {
        ArtistDetailsContent(
            state = sampleArtistDetailsViewState,
            playbackState = samplePlaybackViewState,
            onPlaybackIntent = {},
            onBackClick = {},
            onAlbumClick = {}
        )
    }
}