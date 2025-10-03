package com.example.player.presentation.ui.components.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.player.domain.model.Track
import com.example.player.presentation.ui.components.common.PlayerTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onArtistsClick: () -> Unit = {},
    onPlaylistsClick: () -> Unit = {}
) {
    PlayerTopAppBar(
        title = "Home",
        actions = {
            Row {
                IconButton(onClick = onArtistsClick) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Artists"
                    )
                }
                IconButton(onClick = onPlaylistsClick) {
                    Icon(
                        imageVector = Icons.Default.LibraryMusic,
                        contentDescription = "Playlists"
                    )
                }
            }
        },
    )
}

@Composable
fun FavoritesRow(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(tracks) { track ->
            FavoriteTrackCard(
                track = track,
                onClick = { onTrackClick(track) }
            )
        }
    }
}
