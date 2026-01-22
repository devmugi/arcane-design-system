package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.chat.components.DevicePreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatInputScreen(deviceType: DeviceType) {
    DevicePreview(deviceType = deviceType) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ArcaneSpacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Chat Input Components",
                style = ArcaneTheme.typography.headlineLarge,
                color = ArcaneTheme.colors.text
            )
            Text(
                text = "Coming soon...",
                style = ArcaneTheme.typography.bodyLarge,
                color = ArcaneTheme.colors.textSecondary
            )
        }
    }
}
