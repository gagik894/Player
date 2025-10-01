package com.example.player.presentation.ui.screens

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.player.presentation.mvi.PlaybackIntent
import com.example.player.presentation.mvi.PlaybackViewModel
import com.example.player.presentation.mvi.PlaybackViewState
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.common.EmptyState
import com.example.player.presentation.ui.components.common.ErrorState
import com.example.player.presentation.ui.components.common.LoadingState
import com.example.player.presentation.ui.components.PlayerTopBar
import com.example.player.presentation.ui.layouts.HorizontalPlayerLayout
import com.example.player.presentation.ui.layouts.VerticalPlayerLayout
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    playbackViewModel: PlaybackViewModel
) {
    val viewState by playbackViewModel.viewState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            PlayerTopBar(
                onNavigateBack = onNavigateBack
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
    viewState: PlaybackViewState,
    onIntent: (PlaybackIntent) -> Unit,
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
                    onIntent = onIntent
                )
            } else {
                HorizontalPlayerLayout(
                    viewState = viewState,
                    onIntent = onIntent
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