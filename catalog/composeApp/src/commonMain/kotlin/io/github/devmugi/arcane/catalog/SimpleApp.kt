package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Composable
fun SimpleApp() {
    ArcaneTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Arcane Design System",
                style = ArcaneTheme.typography.headlineLarge,
                color = ArcaneTheme.colors.text
            )
        }
    }
}
