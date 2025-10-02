package com.example.player.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.player.presentation.mvi.PlaylistDetailsViewModel
import com.example.player.presentation.ui.layouts.GenericDetailScreen
import com.example.player.presentation.ui.components.common.TrackListItem

@Composable
fun PlaylistDetailsScreen(
    viewModel: PlaylistDetailsViewModel,
    onTrackClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    
    GenericDetailScreen(
        title = state.playlist?.name ?: "Playlist",
        tracks = state.tracks,
        isLoading = state.isLoading,
        error = state.error,
        onBackClick = onBackClick,
        trackContent = { track ->
            TrackListItem(
                track = track,
                onClick = { onTrackClick(track.id) },
                isSelected = false,
                isPlaying = false,
                onFavoriteClick = { },
            )
        },
        modifier = modifier
    )
}