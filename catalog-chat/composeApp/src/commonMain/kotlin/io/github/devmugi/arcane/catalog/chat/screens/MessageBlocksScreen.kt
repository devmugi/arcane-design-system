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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.catalog.chat.components.DevicePreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun MessageBlocksScreen(deviceType: DeviceType) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors

    DevicePreview(deviceType = deviceType) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(ArcaneSpacing.Medium),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
        ) {
            // User Messages Section
            SectionTitle("User Messages")
            ArcaneSurface(
                variant = SurfaceVariant.Container,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(ArcaneSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
                ) {
                    Text(
                        text = "Short message",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneUserMessageBlock(
                        text = "Hello, Claude!"
                    )

                    Text(
                        text = "Multi-line message",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneUserMessageBlock(
                        text = "Can you help me understand how to implement a chat interface in Compose? I'm looking for best practices.",
                        timestamp = "2:34 PM"
                    )
                }
            }

            // Assistant Messages - Fits Section
            SectionTitle("Assistant Messages - Fits")
            ArcaneSurface(
                variant = SurfaceVariant.Container,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(ArcaneSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
                ) {
                    Text(
                        text = "With title, no loading",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneAssistantMessageBlock(
                        title = "Claude"
                    ) {
                        Text(
                            text = "Hello! I'd be happy to help you with that.",
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }

                    Text(
                        text = "With title + loading",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneAssistantMessageBlock(
                        title = "Claude",
                        isLoading = true
                    ) {
                        Text(
                            text = "Thinking...",
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }

                    Text(
                        text = "With title actions",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneAssistantMessageBlock(
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
                    ) {
                        Text(
                            text = "Here's a helpful response with extra actions.",
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }
                }
            }

            // Assistant Messages - Truncated Section
            SectionTitle("Assistant Messages - Truncated")
            ArcaneSurface(
                variant = SurfaceVariant.Container,
                modifier = Modifier.fillMaxWidth()
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

                    Text(
                        text = "Long message (click Show more)",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneAssistantMessageBlock(
                        title = "Claude",
                        maxContentHeight = 160.dp
                    ) {
                        Text(
                            text = longText,
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }
                }
            }

            // Assistant Messages - Custom Actions Section
            SectionTitle("Assistant Messages - Custom Actions")
            ArcaneSurface(
                variant = SurfaceVariant.Container,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(ArcaneSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
                ) {
                    Text(
                        text = "With bottom actions (always shown)",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneAssistantMessageBlock(
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
                    ) {
                        Text(
                            text = "Here's a response with custom bottom actions always visible.",
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }

                    val longTextWithShare = """
                        This is a longer response that will be truncated and show both the "Show more" action and a share icon in the bottom actions row.

                        The bottom actions appear automatically when content is truncated (default behavior), or can be forced to always show via the showBottomActions parameter.

                        This flexibility allows you to customize the UX based on your needs.
                    """.trimIndent()

                    Text(
                        text = "Truncated with share action",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneAssistantMessageBlock(
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
                    ) {
                        Text(
                            text = longTextWithShare,
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = ArcaneTheme.typography.headlineLarge,
        color = ArcaneTheme.colors.textSecondary
    )
}
