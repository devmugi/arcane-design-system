// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatMessageList.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlinx.coroutines.launch

@Composable
fun <T> ArcaneChatMessageList(
    messages: List<T>,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(ArcaneSpacing.Medium),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(ArcaneSpacing.Medium),
    showScrollToBottom: Boolean = true,
    listState: LazyListState = rememberLazyListState(),
    messageKey: ((T) -> Any)? = null,
    messageContent: @Composable (T) -> Unit
) {
    val colors = ArcaneTheme.colors
    val coroutineScope = rememberCoroutineScope()

    // Determine if we should show scroll-to-bottom button
    val showScrollButton by remember {
        derivedStateOf {
            if (!showScrollToBottom || messages.isEmpty()) {
                false
            } else {
                val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItems = listState.layoutInfo.totalItemsCount
                // Show button if not at bottom (with some threshold)
                totalItems > 0 && lastVisibleIndex < totalItems - 1
            }
        }
    }

    // Auto-scroll to bottom when new messages added (if already at bottom)
    val isAtBottom by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            totalItems == 0 || lastVisibleIndex >= totalItems - 2
        }
    }

    LaunchedEffect(messages.size, isAtBottom) {
        if (messages.isNotEmpty() && isAtBottom) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            reverseLayout = reverseLayout
        ) {
            items(
                items = messages,
                key = messageKey
            ) { message ->
                messageContent(message)
            }
        }

        // Scroll to bottom FAB
        AnimatedVisibility(
            visible = showScrollButton,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = ArcaneSpacing.Medium),
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(150))
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(ArcaneRadius.Full)
                    .background(colors.surfaceRaised)
                    .clickable {
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                    .semantics { contentDescription = "Scroll to bottom" },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = colors.text,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
