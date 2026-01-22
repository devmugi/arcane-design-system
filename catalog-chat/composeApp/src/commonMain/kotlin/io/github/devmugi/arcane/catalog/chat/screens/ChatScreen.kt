package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.chat.components.DevicePreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.catalog.chat.data.MockChatData
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneChatMessageList
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.components.scaffold.ArcaneChatScreenScaffold
import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatScreen(deviceType: DeviceType) {
    val messages = remember { MockChatData.sampleConversation }

    DevicePreview(deviceType = deviceType) {
        ArcaneChatScreenScaffold(
            isEmpty = messages.isEmpty(),
            emptyState = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    Text(
                        text = "No messages yet",
                        style = ArcaneTheme.typography.bodyLarge,
                        color = ArcaneTheme.colors.textSecondary
                    )
                    Text(
                        text = "Start a conversation",
                        style = ArcaneTheme.typography.labelMedium,
                        color = ArcaneTheme.colors.textDisabled
                    )
                }
            }
        ) {
            ArcaneChatMessageList(
                messages = messages,
                messageKey = { it.id }
            ) { message ->
                when (message) {
                    is ChatMessage.User -> ArcaneUserMessageBlock(
                        text = message.text,
                        timestamp = message.timestamp
                    )
                    is ChatMessage.Assistant -> ArcaneAssistantMessageBlock(
                        title = message.title,
                        isLoading = message.isLoading
                    ) {
                        Text(
                            text = message.content,
                            style = ArcaneTheme.typography.bodyMedium,
                            color = ArcaneTheme.colors.text
                        )
                    }
                }
            }
        }
    }
}
