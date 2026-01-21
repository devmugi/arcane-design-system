package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.screens.ChatScreen
import io.github.devmugi.arcane.catalog.screens.ControlsScreen
import io.github.devmugi.arcane.catalog.screens.DataDisplayScreen
import io.github.devmugi.arcane.catalog.screens.DesignSpecScreen
import io.github.devmugi.arcane.catalog.screens.FeedbackScreen
import io.github.devmugi.arcane.catalog.screens.NavigationScreen
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

sealed class Screen {
    data object DesignSpec : Screen()
    data object Controls : Screen()
    data object Navigation : Screen()
    data object DataDisplay : Screen()
    data object Feedback : Screen()
    data object Chat : Screen()
}

@Composable
fun App() {
    ArcaneTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.DesignSpec) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneTheme.colors.surface)
        ) {
            when (currentScreen) {
                Screen.DesignSpec -> DesignSpecScreen(
                    onNavigateToControls = { currentScreen = Screen.Controls },
                    onNavigateToNavigation = { currentScreen = Screen.Navigation },
                    onNavigateToDataDisplay = { currentScreen = Screen.DataDisplay },
                    onNavigateToFeedback = { currentScreen = Screen.Feedback }
                )
                Screen.Controls -> ControlsScreen(
                    onBack = { currentScreen = Screen.DesignSpec }
                )
                Screen.Navigation -> NavigationScreen(
                    onBack = { currentScreen = Screen.DesignSpec }
                )
                Screen.DataDisplay -> DataDisplayScreen(
                    onBack = { currentScreen = Screen.DesignSpec }
                )
                Screen.Feedback -> FeedbackScreen(
                    onBack = { currentScreen = Screen.DesignSpec }
                )
                Screen.Chat -> ChatScreen(
                    onBack = { currentScreen = Screen.DesignSpec }
                )
            }
        }
    }
}
