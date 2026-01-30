package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.devmugi.arcane.design.components.display.ArcaneText
import io.github.devmugi.arcane.design.components.display.ArcaneTextVariant
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.catalog.chat.components.ComponentPreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneOutlinedUserMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.components.messages.OutlinedUserMessageBlockDefaults
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.TextStyle
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun MessageBlocksScreen(deviceType: DeviceType) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XLarge)
    ) {
            // User Messages Section
            PreviewSection(
                title = "User Messages",
                deviceType = deviceType
            ) {
                Column(
                    modifier = Modifier.padding(ArcaneSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
                ) {
                    ArcaneText(
                        text = "Short message",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneUserMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "user-1",
                                content = "Hello, Claude!",
                                style = TextStyle.Body
                            )
                        )
                    )

                    ArcaneText(
                        text = "Multi-line message",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneUserMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "user-2",
                                content = "Can you help me understand how to implement a chat interface in Compose? I'm looking for best practices.",
                                style = TextStyle.Body
                            )
                        ),
                        timestamp = "2:34 PM"
                    )
                }
            }

            // Outlined User Messages Section
            PreviewSection(
                title = "Outlined User Messages",
                deviceType = deviceType
            ) {
                Column(
                    modifier = Modifier.padding(ArcaneSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
                ) {
                    ArcaneText(
                        text = "Default outlined style",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneOutlinedUserMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "outlined-1",
                                content = "Hello, Claude!",
                                style = TextStyle.Body
                            )
                        )
                    )

                    ArcaneText(
                        text = "With timestamp",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneOutlinedUserMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "outlined-2",
                                content = "This is an outlined message with a lighter visual style using border instead of fill.",
                                style = TextStyle.Body
                            )
                        ),
                        timestamp = "2:45 PM"
                    )

                    ArcaneText(
                        text = "Custom border color",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneOutlinedUserMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "outlined-3",
                                content = "Custom border styling",
                                style = TextStyle.Body
                            )
                        ),
                        border = OutlinedUserMessageBlockDefaults.border(
                            color = colors.tertiary,
                            width = 2.dp
                        )
                    )
                }
            }

            // Assistant Messages - Fits Section
            PreviewSection(
                title = "Assistant Messages - Fits",
                deviceType = deviceType
            ) {
                Column(
                    modifier = Modifier.padding(ArcaneSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
                ) {
                    ArcaneText(
                        text = "With title, no loading",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneAssistantMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "asst-1",
                                content = "Hello! I'd be happy to help you with that.",
                                style = TextStyle.Body
                            )
                        ),
                        title = "Claude"
                    )

                    ArcaneText(
                        text = "With title + loading",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneAssistantMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "asst-2",
                                content = "Thinking...",
                                style = TextStyle.Body
                            )
                        ),
                        title = "Claude",
                        isLoading = true
                    )

                    ArcaneText(
                        text = "With title actions",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneAssistantMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "asst-3",
                                content = "Here's a helpful response with extra actions.",
                                style = TextStyle.Body
                            )
                        ),
                        title = "Claude",
                        titleActions = {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Share",
                                tint = colors.textSecondary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { }
                            )
                        }
                    )
                }
            }

            // Assistant Messages - Truncated Section
            PreviewSection(
                title = "Assistant Messages - Truncated",
                deviceType = deviceType
            ) {
                Column(
                    modifier = Modifier.padding(ArcaneSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
                ) {
                    val longText = """
                        I'd be happy to help you understand chat interfaces in Compose! Here are the key concepts:

                        1. **LazyColumn for messages**: Use LazyColumn to efficiently render message lists. It only composes visible items.

                        2. **State management**: Keep your messages in a ViewModel or state holder. Use mutableStateListOf for reactive updates.

                        3. **Message alignment**: User messages typically align right, assistant messages align left.

                        4. **Auto-scroll behavior**: When new messages arrive, scroll to bottom if user was already at bottom. Show a "scroll to bottom" button if scrolled up.

                        5. **Input handling**: Use BasicTextField with keyboard actions for the input area.

                        6. **Loading states**: Show typing indicators or progress when waiting for responses.

                        Would you like me to elaborate on any of these points?
                    """.trimIndent()

                    ArcaneText(
                        text = "Long message (click Show more)",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneAssistantMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "asst-long",
                                content = longText,
                                style = TextStyle.Body
                            )
                        ),
                        title = "Claude",
                        maxContentHeight = 160.dp
                    )
                }
            }

            // Assistant Messages - Custom Actions Section
            PreviewSection(
                title = "Assistant Messages - Custom Actions",
                deviceType = deviceType
            ) {
                Column(
                    modifier = Modifier.padding(ArcaneSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
                ) {
                    ArcaneText(
                        text = "With bottom actions (always shown)",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneAssistantMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "asst-actions",
                                content = "Here's a response with custom bottom actions always visible.",
                                style = TextStyle.Body
                            )
                        ),
                        title = "Claude",
                        showBottomActions = true,
                        autoShowWhenTruncated = false,
                        bottomActions = {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Share",
                                tint = colors.primary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { }
                            )
                        }
                    )

                    val longTextWithShare = """
                        This is a longer response that will be truncated and show both the "Show more" action and a share icon in the bottom actions row.

                        The bottom actions appear automatically when content is truncated (default behavior), or can be forced to always show via the showBottomActions parameter.

                        This flexibility allows you to customize the UX based on your needs.
                    """.trimIndent()

                    ArcaneText(
                        text = "Truncated with share action",
                        variant = ArcaneTextVariant.Secondary,
                        style = typography.labelMedium
                    )
                    ArcaneAssistantMessageBlock(
                        blocks = listOf(
                            MessageBlock.Text(
                                id = "asst-share",
                                content = longTextWithShare,
                                style = TextStyle.Body
                            )
                        ),
                        title = "Claude",
                        maxContentHeight = 100.dp,
                        bottomActions = {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Share",
                                tint = colors.primary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { }
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
        }
}

@Composable
private fun SectionTitle(title: String) {
    ArcaneText(
        text = title,
        variant = ArcaneTextVariant.Secondary,
        style = ArcaneTheme.typography.headlineLarge
    )
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
        SectionTitle(title)
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
