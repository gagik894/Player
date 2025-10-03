package com.example.player.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.player.presentation.mvi.artists.ArtistsIntent
import com.example.player.presentation.mvi.artists.ArtistsViewModel
import com.example.player.presentation.mvi.artists.ArtistsViewState
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.common.ResponsiveInfoCard
import com.example.player.presentation.ui.layouts.GenericListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ArtistsScreen(
    viewModel: ArtistsViewModel,
    onArtistClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.viewState.collectAsState()

    ArtistScreenContent(
        state = state,
        onArtistClick = onArtistClick,
        onNavigateBack = onNavigateBack,
        onArtistsIntent = viewModel::handleIntent,
        modifier = modifier
    )
}


@Composable
private fun ArtistScreenContent(
    state: ArtistsViewState,
    onArtistClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onArtistsIntent: (ArtistsIntent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    GenericListScreen(
        title = "Artists",
        filteredItems = state.filteredArtists,
        searchQuery = state.searchQuery,
        isLoading = state.isLoading,
        error = state.error,
        searchPlaceholder = "Search artists...",
        emptyMessage = "No artists found",
        onSearchQueryChange = { onArtistsIntent(ArtistsIntent.SearchArtists(it)) },
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

@Preview
@Composable
fun ArtistsScreenPreview() {
    val state = ArtistsViewState.sample
    PlayerTheme {
        ArtistScreenContent(
            state = state,
            onArtistClick = {},
            onNavigateBack = {},
        )
    }
}