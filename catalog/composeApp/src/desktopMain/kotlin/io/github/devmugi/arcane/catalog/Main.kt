package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Arcane Design System Catalog"
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneColors.default().surface)
        ) {
            App()
        }
    }
}
