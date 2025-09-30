package com.example.player.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.player.domain.model.Album
import com.example.player.domain.model.Artist
import com.example.player.domain.model.PlaybackState
import com.example.player.domain.model.RepeatMode
import com.example.player.domain.model.Track
import com.example.player.presentation.mvi.PlayerIntent
import com.example.player.presentation.mvi.PlayerViewModel
import com.example.player.presentation.mvi.PlayerViewState
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: PlayerViewModel = viewModel()
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = { PlayerTopBar() }
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
                onIntent = viewModel::handleIntent,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    viewState: PlayerViewState,
    onIntent: (PlayerIntent) -> Unit,
) {
    val currentTrack = viewState.playbackState.currentTrack!!

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ArtworkSection(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(32.dp))

            TrackInfoSection(
                title = currentTrack.title,
                artist = currentTrack.artist.name,
                album = currentTrack.album.title,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            ControlsSection(
                isPlaying = viewState.playbackState.isPlaying,
                isShuffleEnabled = viewState.playbackState.isShuffleEnabled,
                repeatMode = viewState.playbackState.repeatMode,
                onPlayPauseClick = { onIntent(PlayerIntent.PlayPause) },
                onNextClick = { onIntent(PlayerIntent.SkipNext) },
                onPreviousClick = { onIntent(PlayerIntent.SkipPrevious) },
                onShuffleClick = { onIntent(PlayerIntent.ToggleShuffle) },
                onRepeatClick = { onIntent(PlayerIntent.ToggleRepeat) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerScreenPreview() {
    val samepleArtist = Artist(
        id = "1",
        name = "Queen",
        imageUrl = null
    )
    val sampleAlbum = Album(
        id = "1",
        title = "A Night at the Opera",
        artist = samepleArtist,
        year = 1975,
        trackCount = 12
    )
    val sampleTrack = Track(
        id = "1",
        title = "Sample Track",
        artist = samepleArtist,
        album = sampleAlbum,
        duration = Duration.parse("PT3M45S"),
        artworkUrl = "",
        audioUrl = "https://www.example.com/sample.mp3",
        isFavorite = false,
        trackNumber = 1
    )
    val sampleViewState = PlayerViewState(
        isLoading = false,
        error = null,
        playbackState = PlaybackState(
            currentTrack = sampleTrack,
            isPlaying = true,
            isShuffleEnabled = false,
            repeatMode = RepeatMode.OFF
        )
    )
    PlayerTheme {
        PlayerContent(
            viewState = sampleViewState,
            onIntent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}