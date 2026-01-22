package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.github.devmugi.arcane.catalog.chat.components.ComponentPreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.catalog.chat.data.MockChatData
import io.github.devmugi.arcane.chat.components.input.ArcaneAgentChatInput
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneChatMessageList
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.components.scaffold.ArcaneChatScreenScaffold
import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatScreen(deviceType: DeviceType) {
    var messages by remember { mutableStateOf<List<ChatMessage>>(MockChatData.sampleConversation) }
    var inputText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    fun handleSendMessage() {
        if (inputText.isBlank()) return

        // Add user message
        val userMessage = ChatMessage.User(
            id = generateId(),
            text = inputText,
            timestamp = getCurrentTimestamp()
        )
        messages = messages + userMessage
        val userInput = inputText
        inputText = ""

        // Add loading message
        val loadingId = generateId()
        val loadingMessage = ChatMessage.Assistant(
            id = loadingId,
            title = "Assistant",
            content = "",
            isLoading = true,
            timestamp = null
        )
        messages = messages + loadingMessage

        // Simulate response delay
        scope.launch {
            delay(700)
            messages = messages.filterNot { it.id == loadingId }
            val response = ChatMessage.Assistant(
                id = generateId(),
                title = "Assistant",
                content = "Response to \"$userInput\"",
                isLoading = false,
                timestamp = getCurrentTimestamp()
            )
            messages = messages + response
        }
    }

    ComponentPreview(deviceType = deviceType) {
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
            },
            bottomBar = {
                ArcaneAgentChatInput(
                    value = inputText,
                    onValueChange = { inputText = it },
                    onSend = { handleSendMessage() },
                    placeholder = "Type a message...",
                    modifier = Modifier.fillMaxWidth()
                )
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

private var messageIdCounter = 0
private fun generateId(): String = "msg_${messageIdCounter++}"

private fun getCurrentTimestamp(): String {
    // Simple timestamp for demo purposes
    val hour = (System.currentTimeMillis() / 3600000 % 24).toInt()
    val minute = (System.currentTimeMillis() / 60000 % 60).toInt()
    val formattedHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    val amPm = if (hour < 12) "AM" else "PM"
    return String.format("%d:%02d %s", formattedHour, minute, amPm)
}
