package com.example.player.presentation.ui.components.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
) {
    IconButton(
        onClick = onFavoriteClick,
        modifier = modifier.size(40.dp)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
            tint = if (isFavorite) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}