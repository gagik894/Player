package com.example.player.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.player.presentation.mvi.PlaylistsIntent
import com.example.player.presentation.mvi.PlaylistsViewModel
import com.example.player.presentation.ui.components.common.ResponsiveInfoCard
import com.example.player.presentation.ui.layouts.GenericListScreen

@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel,
    onPlaylistClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.viewState.collectAsState()
    
    GenericListScreen(
        title = "Playlists",
        filteredItems = state.filteredPlaylists,
        searchQuery = state.searchQuery,
        isLoading = state.isLoading,
        error = state.error,
        searchPlaceholder = "Search playlists...",
        emptyMessage = "No playlists found",
        onSearchQueryChange = { viewModel.handleIntent(PlaylistsIntent.SearchPlaylists(it)) },
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