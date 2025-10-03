package com.example.player.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.player.presentation.mvi.ArtistsIntent
import com.example.player.presentation.mvi.ArtistsViewModel
import com.example.player.presentation.ui.components.common.ResponsiveInfoCard
import com.example.player.presentation.ui.layouts.GenericListScreen

@Composable
fun ArtistsScreen(
    viewModel: ArtistsViewModel,
    onArtistClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.viewState.collectAsState()
    
    GenericListScreen(
        title = "Artists",
        filteredItems = state.filteredArtists,
        searchQuery = state.searchQuery,
        isLoading = state.isLoading,
        error = state.error,
        searchPlaceholder = "Search artists...",
        emptyMessage = "No artists found",
        onSearchQueryChange = { viewModel.handleIntent(ArtistsIntent.SearchArtists(it)) },
        itemKey = { artist -> artist.id },
        itemContent = { artist ->
            ResponsiveInfoCard(
                title = artist.name,
                subtitle = "Artist",
                description = artist.bio,
                imageUrl = artist.imageUrl,
                onClick = { onArtistClick(artist.id) }
            )
        },
        onBack = onNavigateBack,
        modifier = modifier
    )
}