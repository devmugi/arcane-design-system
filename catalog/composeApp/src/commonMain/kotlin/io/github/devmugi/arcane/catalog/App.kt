// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.screens.ControlsScreen
import io.github.devmugi.arcane.catalog.screens.DataDisplayScreen
import io.github.devmugi.arcane.catalog.screens.NavigationScreen
import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Composable
fun App() {
    ArcaneTheme {
        var selectedScreen by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneTheme.colors.surface)
        ) {
            ArcaneTabs(
                tabs = listOf(
                    ArcaneTab("Controls"),
                    ArcaneTab("Navigation"),
                    ArcaneTab("Data Display")
                ),
                selectedIndex = selectedScreen,
                onTabSelected = { selectedScreen = it }
            )

            when (selectedScreen) {
                0 -> ControlsScreen()
                1 -> NavigationScreen()
                2 -> DataDisplayScreen()
            }
        }
    }
}
