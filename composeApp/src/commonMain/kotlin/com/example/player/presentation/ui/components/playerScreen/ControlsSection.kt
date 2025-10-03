package com.example.player.presentation.ui.components.playerScreen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.player.domain.model.RepeatMode
import com.example.player.presentation.theme.PlayerTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ControlsSection(
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    isShuffleEnabled: Boolean,
    onShuffleClick: () -> Unit,
    repeatMode: RepeatMode,
    onRepeatClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shuffle button
        ToggleIconButton(
            icon = Icons.Default.Shuffle,
            isEnabled = isShuffleEnabled,
            onClick = onShuffleClick,
            contentDescription = "Shuffle"
        )

        // Previous button
        NavigationButton(
            icon = Icons.Default.SkipPrevious,
            onClick = onPreviousClick,
            contentDescription = "Previous"
        )

        // Play/Pause button
        PlayPauseButton(
            isPlaying = isPlaying,
            onClick = onPlayPauseClick
        )

        // Next button
        NavigationButton(
            icon = Icons.Default.SkipNext,
            onClick = onNextClick,
            contentDescription = "Next"
        )

        // Repeat button
        val (repeatIcon, isRepeatEnabled) = when (repeatMode) {
            RepeatMode.OFF -> Icons.Default.Repeat to false
            RepeatMode.ALL -> Icons.Default.Repeat to true
            RepeatMode.ONE -> Icons.Default.RepeatOne to true
        }

        ToggleIconButton(
            icon = repeatIcon,
            isEnabled = isRepeatEnabled,
            onClick = onRepeatClick,
            contentDescription = "Repeat"
        )
    }
}

@Composable
private fun NavigationButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun ToggleIconButton(
    icon: ImageVector,
    isEnabled: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isEnabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
private fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerRadius by animateDpAsState(
        targetValue = if (isPlaying) 8.dp else 36.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    FilledIconButton(
        onClick = onClick,
        modifier = modifier.size(72.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            modifier = Modifier.size(36.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ControlsSectionPreview() {
    PlayerTheme {
        var isPlaying by remember { mutableStateOf(false) }
        ControlsSection(
            isPlaying = isPlaying,
            onPlayPauseClick = {
                isPlaying = !isPlaying
            },
            isShuffleEnabled = false,
            onShuffleClick = {},
            repeatMode = RepeatMode.ALL,
            onRepeatClick = {},
            onPreviousClick = {},
            onNextClick = {}
        )
    }
}