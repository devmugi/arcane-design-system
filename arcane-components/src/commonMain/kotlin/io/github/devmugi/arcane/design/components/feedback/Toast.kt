package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlinx.coroutines.delay

@Immutable
sealed class ArcaneToastStyle {
    data object Default : ArcaneToastStyle()
    data object Success : ArcaneToastStyle()
    data object Warning : ArcaneToastStyle()
    data object Error : ArcaneToastStyle()
}

enum class ArcaneToastPosition {
    TopStart, TopCenter, TopEnd,
    BottomStart, BottomCenter, BottomEnd
}

@Immutable
data class ArcaneToastData(
    val id: Long = System.currentTimeMillis(),
    val message: String,
    val style: ArcaneToastStyle = ArcaneToastStyle.Default,
    val durationMs: Long = 4000L
)

class ArcaneToastState {
    internal val toasts: SnapshotStateList<ArcaneToastData> = mutableStateListOf()

    fun show(message: String, style: ArcaneToastStyle = ArcaneToastStyle.Default, durationMs: Long = 4000L) {
        show(ArcaneToastData(message = message, style = style, durationMs = durationMs))
    }

    fun show(toast: ArcaneToastData) {
        // Limit to 3 visible toasts
        if (toasts.size >= 3) {
            toasts.removeAt(0)
        }
        toasts.add(toast)
    }

    fun dismiss(id: Long) {
        toasts.removeAll { it.id == id }
    }

    fun dismissAll() {
        toasts.clear()
    }
}

@Composable
fun rememberArcaneToastState(): ArcaneToastState {
    return remember { ArcaneToastState() }
}

val LocalArcaneToastState = compositionLocalOf<ArcaneToastState> {
    error("No ArcaneToastState provided. Wrap your content with ArcaneToastHost.")
}

@Composable
fun ArcaneToastHost(
    state: ArcaneToastState,
    modifier: Modifier = Modifier,
    position: ArcaneToastPosition = ArcaneToastPosition.BottomCenter
) {
    val alignment = when (position) {
        ArcaneToastPosition.TopStart -> Alignment.TopStart
        ArcaneToastPosition.TopCenter -> Alignment.TopCenter
        ArcaneToastPosition.TopEnd -> Alignment.TopEnd
        ArcaneToastPosition.BottomStart -> Alignment.BottomStart
        ArcaneToastPosition.BottomCenter -> Alignment.BottomCenter
        ArcaneToastPosition.BottomEnd -> Alignment.BottomEnd
    }

    val isTop = position in listOf(
        ArcaneToastPosition.TopStart,
        ArcaneToastPosition.TopCenter,
        ArcaneToastPosition.TopEnd
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier.padding(ArcaneSpacing.Medium),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
            horizontalAlignment = when (position) {
                ArcaneToastPosition.TopStart, ArcaneToastPosition.BottomStart -> Alignment.Start
                ArcaneToastPosition.TopCenter, ArcaneToastPosition.BottomCenter -> Alignment.CenterHorizontally
                ArcaneToastPosition.TopEnd, ArcaneToastPosition.BottomEnd -> Alignment.End
            }
        ) {
            state.toasts.forEach { toast ->
                androidx.compose.runtime.key(toast.id) {
                    ArcaneToastItem(
                        toast = toast,
                        isTop = isTop,
                        onDismiss = { state.dismiss(toast.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ArcaneToastItem(
    toast: ArcaneToastData,
    isTop: Boolean,
    onDismiss: () -> Unit
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val accentColor = when (toast.style) {
        is ArcaneToastStyle.Default -> colors.primary
        is ArcaneToastStyle.Success -> colors.success
        is ArcaneToastStyle.Warning -> colors.warning
        is ArcaneToastStyle.Error -> colors.error
    }

    LaunchedEffect(toast.id) {
        delay(toast.durationMs)
        onDismiss()
    }

    val shape = ArcaneRadius.Small

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { if (isTop) -it else it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { if (isTop) -it else it },
            animationSpec = tween(200)
        ) + fadeOut(animationSpec = tween(200))
    ) {
        Row(
            modifier = Modifier
                .widthIn(min = 200.dp, max = 400.dp)
                .clip(shape)
                .background(colors.surfaceRaised)
                .border(1.dp, accentColor.copy(alpha = 0.5f), shape)
                .padding(
                    start = ArcaneSpacing.Medium,
                    end = ArcaneSpacing.Small,
                    top = ArcaneSpacing.Small,
                    bottom = ArcaneSpacing.Small
                ),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = toast.message,
                style = typography.bodyMedium,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss
                    ),
                tint = colors.textSecondary
            )
        }
    }
}

