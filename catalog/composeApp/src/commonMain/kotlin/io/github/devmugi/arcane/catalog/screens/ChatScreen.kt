// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ChatScreen.kt
package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.design.components.controls.ArcaneChatScreenScaffold
import io.github.devmugi.arcane.design.components.controls.ArcaneUserMessageBlock
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatScreen(onBack: () -> Unit = {}) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = colors.primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )
            Text(
                text = "Chat",
                style = typography.displayMedium,
                color = colors.text
            )
        }

        // Empty State Section
        SectionTitle("Empty State")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(ArcaneSpacing.Medium)
            ) {
                ArcaneChatScreenScaffold(
                    isEmpty = true,
                    emptyState = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                        ) {
                            Text(
                                text = "No messages yet",
                                style = typography.bodyLarge,
                                color = colors.textSecondary
                            )
                            Text(
                                text = "Start a conversation",
                                style = typography.labelMedium,
                                color = colors.textDisabled
                            )
                        }
                    },
                    content = {}
                )
            }
        }

        // User Messages Section
        SectionTitle("User Messages")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
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
            variant = SurfaceVariant.Raised,
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
                    text = "Hello! I'd be happy to help you with that.",
                    title = "Claude",
                    onCopyClick = {}
                )

                Text(
                    text = "With title + loading",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAssistantMessageBlock(
                    text = "Thinking...",
                    title = "Claude",
                    isLoading = true,
                    onCopyClick = {}
                )

                Text(
                    text = "With title actions",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAssistantMessageBlock(
                    text = "Here's a helpful response with extra actions.",
                    title = "Claude",
                    onCopyClick = {},
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
        SectionTitle("Assistant Messages - Truncated")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
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
                    text = longText,
                    title = "Claude",
                    maxContentHeight = 160.dp,
                    onCopyClick = {}
                )
            }
        }

        // Assistant Messages - Custom Actions Section
        SectionTitle("Assistant Messages - Custom Actions")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
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
                    text = "Here's a response with custom bottom actions always visible.",
                    title = "Claude",
                    showBottomActions = true,
                    autoShowWhenTruncated = false,
                    onCopyClick = {},
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

                Text(
                    text = "Truncated with share action",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAssistantMessageBlock(
                    text = longTextWithShare,
                    title = "Claude",
                    maxContentHeight = 100.dp,
                    onCopyClick = {},
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
    Text(
        text = title,
        style = ArcaneTheme.typography.headlineLarge,
        color = ArcaneTheme.colors.textSecondary
    )
}
