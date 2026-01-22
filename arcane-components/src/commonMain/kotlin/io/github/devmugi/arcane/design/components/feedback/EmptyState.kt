package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneEmptyState(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = ArcaneTheme.colors

    ArcaneSurface(
        variant = SurfaceVariant.ContainerLowest,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colors.border.copy(alpha = 0.5f),
                shape = ArcaneRadius.Medium
            ),
        showBorder = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArcaneSpacing.XLarge),
            horizontalAlignment = contentAlignment,
            content = content
        )
    }
}
