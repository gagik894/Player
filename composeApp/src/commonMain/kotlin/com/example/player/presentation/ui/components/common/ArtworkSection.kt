package com.example.player.presentation.ui.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.player.presentation.theme.PlayerTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ArtworkSection(
    modifier: Modifier = Modifier,
    artworkUrl: String? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(10),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            if (artworkUrl != null) {
                // TODO: Load actual artwork from URL
                PlaceholderArtwork()
            } else {
                PlaceholderArtwork()
            }
        }
    }
}

@Composable
private fun PlaceholderArtwork() {
    Icon(
        imageVector = Icons.Default.MusicNote,
        contentDescription = null,
        modifier = Modifier.size(80.dp),
        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
    )
}

@Preview
@Composable
private fun ArtworkSectionPreview() {
    PlayerTheme {
        ArtworkSection()
    }
}