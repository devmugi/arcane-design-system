package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import io.github.devmugi.arcane.design.components.display.ArcaneText
import io.github.devmugi.arcane.design.components.display.ArcaneTextVariant
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.chat.components.ComponentPreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.chat.components.input.ArcaneAgentChatInput
import io.github.devmugi.arcane.chat.components.input.ArcaneFloatingInputContainer
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatInputScreen(deviceType: DeviceType) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XLarge)
    ) {
        // Empty state
        PreviewSection(
            title = "Empty State",
            deviceType = deviceType
        ) {
            var emptyText by remember { mutableStateOf("") }
            ArcaneAgentChatInput(
                value = emptyText,
                onValueChange = { emptyText = it },
                onSend = { emptyText = "" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ArcaneSpacing.Medium)
            )
        }

        // With text
        PreviewSection(
            title = "With Text",
            deviceType = deviceType
        ) {
            var textContent by remember { mutableStateOf("Hello, Claude!") }
            ArcaneAgentChatInput(
                value = textContent,
                onValueChange = { textContent = it },
                onSend = { textContent = "" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ArcaneSpacing.Medium)
            )
        }

        // Disabled
        PreviewSection(
            title = "Disabled",
            deviceType = deviceType
        ) {
            ArcaneAgentChatInput(
                value = "",
                onValueChange = {},
                onSend = {},
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ArcaneSpacing.Medium)
            )
        }

        // Focus Animation
        PreviewSection(
            title = "Focus Animation",
            deviceType = deviceType
        ) {
            var animatedText by remember { mutableStateOf("") }
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                ArcaneText(
                    text = "Tap to see width expand on focus",
                    variant = ArcaneTextVariant.Secondary,
                    style = typography.labelMedium,
                    modifier = Modifier.padding(bottom = ArcaneSpacing.Small)
                )
                ArcaneAgentChatInput(
                    value = animatedText,
                    onValueChange = { animatedText = it },
                    onSend = { animatedText = "" },
                    animateFocus = true,
                    focusWidthFraction = 0.85f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Floating Container
        PreviewSection(
            title = "Floating Input Container",
            deviceType = deviceType
        ) {
            var floatingText by remember { mutableStateOf("") }
            ArcaneFloatingInputContainer(
                modifier = Modifier.fillMaxWidth()
            ) {
                ArcaneAgentChatInput(
                    value = floatingText,
                    onValueChange = { floatingText = it },
                    onSend = { floatingText = "" },
                    animateFocus = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun PreviewSection(
    title: String,
    deviceType: DeviceType,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
    ) {
        ArcaneText(
            text = title,
            variant = ArcaneTextVariant.Secondary,
            style = ArcaneTheme.typography.headlineLarge
        )
        ComponentPreview(deviceType = deviceType) {
            ArcaneSurface(
                variant = SurfaceVariant.Container,
                modifier = Modifier.fillMaxWidth()
            ) {
                content()
            }
        }
    }
}
