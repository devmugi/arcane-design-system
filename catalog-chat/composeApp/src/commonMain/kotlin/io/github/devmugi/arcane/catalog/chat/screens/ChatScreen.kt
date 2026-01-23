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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import io.github.devmugi.arcane.catalog.chat.components.ComponentPreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.catalog.chat.data.MockChatData
import io.github.devmugi.arcane.chat.components.input.ArcaneAgentChatInput
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneChatMessageList
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.components.scaffold.ArcaneChatScreenScaffold
import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.Suggestion
import io.github.devmugi.arcane.chat.models.TextStyle
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatScreen(deviceType: DeviceType) {
    var messages by remember { mutableStateOf<List<ChatMessage>>(MockChatData.conversationWithSuggestions) }
    var inputText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    fun handleSuggestionClick(suggestionText: String) {
        // Add loading message
        val loadingId = generateId()
        val loadingMessage = ChatMessage.Assistant(
            id = loadingId,
            title = "Assistant",
            blocks = listOf(
                MessageBlock.Text(
                    id = generateId(),
                    content = "",
                    style = TextStyle.Body
                )
            ),
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
                blocks = listOf(
                    MessageBlock.Text(
                        id = generateId(),
                        content = "Great choice! Let me tell you more about $suggestionText...\n\nThis is a simulated response showing how suggestion interactions work in the chat interface.",
                        style = TextStyle.Body
                    )
                ),
                isLoading = false,
                timestamp = getCurrentTimestamp()
            )
            messages = messages + response
        }
    }

    fun handleSendMessage() {
        if (inputText.isBlank()) return

        // Add user message
        val userMessage = ChatMessage.User(
            id = generateId(),
            blocks = listOf(
                MessageBlock.Text(
                    id = generateId(),
                    content = inputText,
                    style = TextStyle.Body
                )
            ),
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
            blocks = listOf(
                MessageBlock.Text(
                    id = generateId(),
                    content = "",
                    style = TextStyle.Body
                )
            ),
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
                blocks = listOf(
                    MessageBlock.Text(
                        id = generateId(),
                        content = "Response to \"$userInput\"",
                        style = TextStyle.Body
                    )
                ),
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
                // Map blocks to inject suggestion callback
                val blocksWithCallback = message.blocks.map { block ->
                    if (block is MessageBlock.AgentSuggestions) {
                        MessageBlock.AgentSuggestions(
                            id = block.id,
                            suggestions = block.suggestions,
                            onSuggestionSelected = { suggestion ->
                                handleSuggestionClick(suggestion.text)
                            }
                        )
                    } else {
                        block
                    }
                }

                when (message) {
                    is ChatMessage.User -> ArcaneUserMessageBlock(
                        blocks = blocksWithCallback,
                        timestamp = message.timestamp
                    )
                    is ChatMessage.Assistant -> ArcaneAssistantMessageBlock(
                        blocks = blocksWithCallback,
                        title = message.title,
                        isLoading = message.isLoading
                    )
                }
            }
        }
    }
}

private var messageIdCounter = 0
private fun generateId(): String = "msg_${messageIdCounter++}"

private fun getCurrentTimestamp(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = now.hour
    val minute = now.minute
    val formattedHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    val amPm = if (hour < 12) "AM" else "PM"
    val minuteStr = if (minute < 10) "0$minute" else "$minute"
    return "$formattedHour:$minuteStr $amPm"
}
