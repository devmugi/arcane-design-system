package io.github.devmugi.arcane.catalog.chat

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Arcane Chat Catalog"
    ) {
        App()
    }
}
