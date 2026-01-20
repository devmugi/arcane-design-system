package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun App() {
    ArcaneTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ArcaneSpacing.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
        ) {
            Text(
                text = "Arcane Design System",
                style = ArcaneTheme.typography.displayMedium,
                color = ArcaneTheme.colors.text
            )

            Text(
                text = "Catalog App",
                style = ArcaneTheme.typography.bodyLarge,
                color = ArcaneTheme.colors.textSecondary
            )

            ArcaneSurface(
                variant = SurfaceVariant.Base,
                showGlow = true,
                modifier = Modifier.padding(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "Base Surface",
                    style = ArcaneTheme.typography.bodyMedium,
                    color = ArcaneTheme.colors.text,
                    modifier = Modifier.padding(ArcaneSpacing.Medium)
                )
            }

            ArcaneSurface(
                variant = SurfaceVariant.Raised,
                showGlow = true,
                modifier = Modifier.padding(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "Raised Surface",
                    style = ArcaneTheme.typography.bodyMedium,
                    color = ArcaneTheme.colors.text,
                    modifier = Modifier.padding(ArcaneSpacing.Medium)
                )
            }

            ArcaneSurface(
                variant = SurfaceVariant.Inset,
                modifier = Modifier.padding(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "Inset Surface",
                    style = ArcaneTheme.typography.bodyMedium,
                    color = ArcaneTheme.colors.text,
                    modifier = Modifier.padding(ArcaneSpacing.Medium)
                )
            }

            ArcaneSurface(
                variant = SurfaceVariant.Pressed,
                modifier = Modifier.padding(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "Pressed Surface",
                    style = ArcaneTheme.typography.bodyMedium,
                    color = ArcaneTheme.colors.text,
                    modifier = Modifier.padding(ArcaneSpacing.Medium)
                )
            }
        }
    }
}
