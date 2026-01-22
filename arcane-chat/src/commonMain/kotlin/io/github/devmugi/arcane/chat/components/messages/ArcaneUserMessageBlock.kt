package io.github.devmugi.arcane.chat.components.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

private val UserMessageShape = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 4.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

@Composable
fun ArcaneUserMessageBlock(
    text: String,
    modifier: Modifier = Modifier,
    timestamp: String? = null,
    maxWidth: Dp = 280.dp,
    backgroundColor: Color = ArcaneTheme.colors.primary.copy(alpha = 0.15f),
    textStyle: TextStyle = ArcaneTheme.typography.bodyMedium
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .clip(UserMessageShape)
                .background(backgroundColor)
                .padding(
                    horizontal = ArcaneSpacing.Small,
                    vertical = ArcaneSpacing.XSmall
                ),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = text,
                style = textStyle,
                color = colors.text
            )
            if (timestamp != null) {
                Text(
                    text = timestamp,
                    style = typography.labelSmall,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(top = ArcaneSpacing.XXSmall)
                )
            }
        }
    }
}
