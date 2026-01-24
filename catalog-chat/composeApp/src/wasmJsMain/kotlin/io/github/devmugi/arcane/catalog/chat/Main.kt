package io.github.devmugi.arcane.catalog.chat

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Setup JavaScript bridge for browser automation
    setupChatBridge()

    val targetElement = document.getElementById("ComposeTarget") ?: document.body!!
    ComposeViewport(targetElement) {
        App()
    }
}
