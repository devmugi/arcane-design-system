// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.screens.ControlsScreen
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Composable
fun App() {
    ArcaneTheme {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneTheme.colors.surface)
        ) {
            ControlsScreen()
        }
    }
}
