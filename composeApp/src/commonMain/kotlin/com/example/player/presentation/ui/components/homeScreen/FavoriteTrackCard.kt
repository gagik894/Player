package com.example.player.presentation.ui.components.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.player.domain.model.Track
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.common.ArtworkSection
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavoriteTrackCard(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            ArtworkSection(
                artworkUrl = track.artworkUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = track.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = track.artist.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun RecentTrackCardPreview() {
    PlayerTheme {
        FavoriteTrackCard(
            track = Track.sample,
            onClick = {}
        )
    }
}
