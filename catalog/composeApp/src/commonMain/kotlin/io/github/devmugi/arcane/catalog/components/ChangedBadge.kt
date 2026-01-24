package io.github.devmugi.arcane.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Composable
fun ChangedBadge(
    changeType: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, label) = when (changeType) {
        "added" -> Triple(
            Color(0xFF238636),
            Color.White,
            "NEW"
        )
        "modified" -> Triple(
            ArcaneTheme.colors.primary,
            Color.White,
            "CHANGED"
        )
        else -> Triple(
            ArcaneTheme.colors.surfaceContainer,
            ArcaneTheme.colors.textSecondary,
            changeType.uppercase()
        )
    }

    Text(
        text = label,
        style = ArcaneTheme.typography.labelSmall,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
