package com.example.player.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.player.presentation.mvi.playLists.PlaylistsIntent
import com.example.player.presentation.mvi.playLists.PlaylistsViewModel
import com.example.player.presentation.mvi.playLists.PlaylistsViewState
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.common.ResponsiveInfoCard
import com.example.player.presentation.ui.layouts.GenericListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel,
    onPlaylistClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.viewState.collectAsState()

    PlaylistsScreenContent(
        state = state,
        onPlaylistClick = onPlaylistClick,
        onNavigateBack = onNavigateBack,
        onPlayListIntent = viewModel::handleIntent,
        modifier = modifier
    )
}

@Composable
private fun PlaylistsScreenContent(
    state: PlaylistsViewState,
    onPlaylistClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onPlayListIntent: (PlaylistsIntent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    GenericListScreen(
        title = "Playlists",
        filteredItems = state.filteredPlaylists,
        searchQuery = state.searchQuery,
        isLoading = state.isLoading,
        error = state.error,
        searchPlaceholder = "Search playlists...",
        emptyMessage = "No playlists found",
        onSearchQueryChange = { onPlayListIntent(PlaylistsIntent.SearchPlaylists(it)) },
        itemKey = { playlist -> playlist.id },
        itemContent = { playlist ->
            ResponsiveInfoCard(
                title = playlist.name,
                subtitle = "${playlist.tracks.size} songs",
                description = playlist.description,
                imageUrl = playlist.coverUrl,
                onClick = { onPlaylistClick(playlist.id) }
            )
        },
        onBack = onNavigateBack,
        modifier = modifier
    )
}

@Preview
@Composable
fun PlaylistsScreenPreview() {
    val state = PlaylistsViewState.sample
    PlayerTheme {
        PlaylistsScreenContent(
            state = state,
            onPlaylistClick = {},
            onNavigateBack = {}
        )
    }
}