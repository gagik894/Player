package com.example.player.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.player.presentation.mvi.playBack.PlaybackIntent
import com.example.player.presentation.mvi.playBack.PlaybackViewModel
import com.example.player.presentation.mvi.playBack.PlaybackViewState
import com.example.player.presentation.mvi.queue.QueueViewModel
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.common.EmptyState
import com.example.player.presentation.ui.components.common.ErrorState
import com.example.player.presentation.ui.components.common.LoadingState
import com.example.player.presentation.ui.components.common.PlayerTopAppBar
import com.example.player.presentation.ui.components.playerScreen.QueueOverlay
import com.example.player.presentation.ui.layouts.HorizontalPlayerLayout
import com.example.player.presentation.ui.layouts.VerticalPlayerLayout
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onAlbumClick: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {},
    playbackViewModel: PlaybackViewModel,
    queueViewModel: QueueViewModel
) {
    val viewState by playbackViewModel.viewState.collectAsStateWithLifecycle()
    val queueViewState by queueViewModel.viewState.collectAsStateWithLifecycle()
    var isQueueVisible by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                PlayerTopAppBar(
                    title = viewState.playbackState.currentTrack?.title ?: "Player",
                    onBack = onNavigateBack,
                    actions = {
                        IconButton(onClick = { isQueueVisible = true }) {
                            Icon(Icons.AutoMirrored.Filled.QueueMusic, contentDescription = "Queue")
                        }
                    }
                )
            }
        ) { paddingValues ->
            when {
                viewState.isLoading -> LoadingState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )

                viewState.error != null -> ErrorState(
                    error = viewState.error ?: "Unknown Error",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )

                viewState.playbackState.currentTrack == null -> EmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )

                else -> PlayerContent(
                    viewState = viewState,
                    onIntent = playbackViewModel::handleIntent,
                    onAlbumClick = onAlbumClick,
                    onArtistClick = onArtistClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }

        // Queue Overlay
        QueueOverlay(
            isVisible = isQueueVisible,
            viewState = queueViewState,
            onDismiss = { isQueueVisible = false },
            onTrackClick = { track ->
                // Play the selected track from the queue context
                playbackViewModel.handleIntent(
                    PlaybackIntent.PlayTrackFromContext(track, queueViewState.queue)
                )
                isQueueVisible = false
            },
            onFavoriteClick = { track ->
                playbackViewModel.handleIntent(PlaybackIntent.ToggleFavorite(track.id))
            }
        )
    }
}

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    viewState: PlaybackViewState,
    onIntent: (PlaybackIntent) -> Unit,
    onAlbumClick: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {}
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints {
            val isPortrait = maxHeight > maxWidth

            if (isPortrait) {
                VerticalPlayerLayout(
                    viewState = viewState,
                    onIntent = onIntent,
                    onAlbumClick = onAlbumClick,
                    onArtistClick = onArtistClick
                )
            } else {
                HorizontalPlayerLayout(
                    viewState = viewState,
                    onIntent = onIntent,
                    onAlbumClick = onAlbumClick,
                    onArtistClick = onArtistClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerScreenPreview() {
    PlayerTheme {
        PlayerContent(
            viewState = PlaybackViewState.sample,
            onIntent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}