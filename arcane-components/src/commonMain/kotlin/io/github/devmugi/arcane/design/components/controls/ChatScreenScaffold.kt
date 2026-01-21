// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatScreenScaffold.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ArcaneChatScreenScaffold(
    isEmpty: Boolean,
    modifier: Modifier = Modifier,
    emptyState: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(
            targetState = isEmpty,
            animationSpec = tween(150),
            label = "ChatScaffoldCrossfade"
        ) { showEmpty ->
            if (showEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    emptyState()
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    content()
                }
            }
        }
    }
}
