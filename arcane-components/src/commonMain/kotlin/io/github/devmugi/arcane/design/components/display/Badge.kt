// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Badge.kt
package io.github.devmugi.arcane.design.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
sealed class ArcaneBadgeStyle {
    data object Default : ArcaneBadgeStyle()
    data object Success : ArcaneBadgeStyle()
    data object Warning : ArcaneBadgeStyle()
    data object Error : ArcaneBadgeStyle()
    data object Neutral : ArcaneBadgeStyle()
    data class Custom(
        val backgroundColor: Color,
        val contentColor: Color
    ) : ArcaneBadgeStyle()
}

@Composable
fun ArcaneBadge(
    text: String,
    modifier: Modifier = Modifier,
    style: ArcaneBadgeStyle = ArcaneBadgeStyle.Default
) {
    val colors = ArcaneTheme.colors

    val (backgroundColor, contentColor) = when (style) {
        is ArcaneBadgeStyle.Default -> colors.primary.copy(alpha = 0.2f) to colors.primary
        is ArcaneBadgeStyle.Success -> colors.success.copy(alpha = 0.2f) to colors.success
        is ArcaneBadgeStyle.Warning -> colors.warning.copy(alpha = 0.2f) to colors.warning
        is ArcaneBadgeStyle.Error -> colors.error.copy(alpha = 0.2f) to colors.error
        is ArcaneBadgeStyle.Neutral -> colors.textSecondary.copy(alpha = 0.2f) to colors.textSecondary
        is ArcaneBadgeStyle.Custom -> style.backgroundColor to style.contentColor
    }

    val borderColor = contentColor.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .height(20.dp)
            .background(backgroundColor, ArcaneRadius.Small)
            .border(ArcaneBorder.Title, borderColor, ArcaneRadius.Small)
            .padding(horizontal = ArcaneSpacing.XSmall),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = ArcaneTheme.typography.labelSmall,
            color = contentColor
        )
    }
}
