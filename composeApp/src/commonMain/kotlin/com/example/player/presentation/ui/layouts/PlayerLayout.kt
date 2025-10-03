package com.example.player.presentation.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.player.presentation.mvi.playBack.PlaybackIntent
import com.example.player.presentation.mvi.playBack.PlaybackViewState
import com.example.player.presentation.ui.components.common.ArtworkSection
import com.example.player.presentation.ui.components.playerScreen.ControlsSection
import com.example.player.presentation.ui.components.playerScreen.ProgressSection
import com.example.player.presentation.ui.components.playerScreen.TrackInfoSection
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VerticalPlayerLayout(
    viewState: PlaybackViewState,
    onIntent: (PlaybackIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ArtworkSection(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.height(48.dp))

        PlayerInfoAndControls(
            viewState = viewState,
            onIntent = onIntent
        )
    }
}

@Composable
fun HorizontalPlayerLayout(
    viewState: PlaybackViewState,
    onIntent: (PlaybackIntent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ArtworkSection(
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.width(48.dp))

        PlayerInfoAndControls(
            modifier = Modifier
                .weight(1f),
            viewState = viewState,
            onIntent = onIntent
        )
    }
}


/**
 * A shared composable that groups the track info, progress, and control buttons.
 * This removes duplication between the vertical and horizontal layouts.
 */
@Composable
private fun PlayerInfoAndControls(
    modifier: Modifier = Modifier,
    viewState: PlaybackViewState,
    onIntent: (PlaybackIntent) -> Unit
) {
    val currentTrack = viewState.playbackState.currentTrack
        ?: return // If there's no track, don't display anything

    Column(modifier = modifier) {
        TrackInfoSection(
            title = currentTrack.title,
            artist = currentTrack.artist.name,
            album = currentTrack.album.title,
            isFavorite = currentTrack.isFavorite,
            onFavoriteClick = { onIntent(PlaybackIntent.ToggleFavorite(currentTrack.id)) }
        )

        Spacer(modifier = Modifier.weight(1f, fill = false))

        ProgressSection(
            modifier = Modifier.fillMaxWidth(),
            currentPosition = viewState.progress,
            onPositionChange = { onIntent(PlaybackIntent.SeekTo(viewState.getDurationFromProgress(it))) },
            currentTime = viewState.currentTime,
            totalDuration = viewState.playbackState.totalDuration,
            isPlaying = viewState.playbackState.isPlaying
        )

        Spacer(modifier = Modifier.height(32.dp))

        ControlsSection(
            isPlaying = viewState.playbackState.isPlaying,
            isShuffleEnabled = viewState.playbackState.isShuffleEnabled,
            repeatMode = viewState.playbackState.repeatMode,
            onPlayPauseClick = { onIntent(PlaybackIntent.PlayPause) },
            onNextClick = { onIntent(PlaybackIntent.SkipNext) },
            onPreviousClick = { onIntent(PlaybackIntent.SkipPrevious) },
            onShuffleClick = { onIntent(PlaybackIntent.ToggleShuffle) },
            onRepeatClick = { onIntent(PlaybackIntent.ToggleRepeat) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VerticalPlayerLayoutPreview() {
    val sampleViewState = PlaybackViewState.sample
    VerticalPlayerLayout(
        viewState = sampleViewState,
        onIntent = {}
    )
}
@Preview(showBackground = true, widthDp = 700, heightDp = 400)
@Composable
private fun HorizontalPlayerLayoutPreview() {
    val sampleViewState = PlaybackViewState.sample
    HorizontalPlayerLayout(
        viewState = sampleViewState,
        onIntent = {}
    )
}