package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun prChangesMain() {
    val targetElement = document.getElementById("ComposeTarget") ?: document.body!!
    ComposeViewport(targetElement) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneColors.default().surfaceContainerLow)
        ) {
            App(isFilteredMode = true)
        }
    }
}
