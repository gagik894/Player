package com.example.player.presentation.ui.components.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.player.domain.model.Track
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.util.toTimeString
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TrackListItem(
    track: Track,
    isSelected: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation for currently playing indicator
    val scaleAnimation by animateFloatAsState(
        targetValue = if (isSelected && isPlaying) 1.1f else 1f,
        label = "playing_scale"
    )
    
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArtworkSection(
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 8.dp),
                artworkUrl = track.artworkUrl
            )
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        contentColor
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${track.artist.name} • ${track.album.title}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .graphicsLayer(
                        scaleX = scaleAnimation,
                        scaleY = scaleAnimation
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = if (isPlaying) Icons.AutoMirrored.Filled.VolumeUp else Icons.Default.Pause,
                        contentDescription = if (isPlaying) "Playing" else "Paused",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = track.duration.toTimeString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            
            FavoriteButton(
                isFavorite = track.isFavorite,
                onFavoriteClick = onFavoriteClick,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackListItemPreview() {
    PlayerTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            
            //Selected Currently playing
            TrackListItem(
                track = Track.sample,
                isSelected = true,
                isPlaying = true,
                onClick = {},
                onFavoriteClick = {}
            )
            // Selected Paused
            TrackListItem(
                track = Track.sample,
                isSelected = true,
                isPlaying = false,
                onClick = {},
                onFavoriteClick = {}
            )
            // Regular track
            TrackListItem(
                track = Track.sample2,
                isSelected = false,
                isPlaying = false,
                onClick = {},
                onFavoriteClick = {}
            )
        }
    }
}