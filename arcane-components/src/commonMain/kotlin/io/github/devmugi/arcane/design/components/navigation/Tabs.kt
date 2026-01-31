package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.modifiers.arcaneGlowIf
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneIconography
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneMotion
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
sealed class ArcaneTabStyle {
    data object Filled : ArcaneTabStyle()
    data object Underline : ArcaneTabStyle()
}

@Immutable
data class ArcaneTab(
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
    val enabled: Boolean = true
)

@Composable
internal fun ArcaneTabItem(
    tab: ArcaneTab,
    selected: Boolean,
    onClick: () -> Unit,
    style: ArcaneTabStyle,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val isFilled = style is ArcaneTabStyle.Filled

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !tab.enabled -> Color.Transparent
            isPressed -> colors.surfaceContainerHigh
            selected && isFilled -> colors.primary
            else -> Color.Transparent
        },
        animationSpec = tween(ArcaneMotion.Fast),
        label = "tabBackgroundColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !tab.enabled -> colors.textDisabled
            selected && isFilled -> colors.surface
            selected -> colors.primary
            else -> colors.textSecondary
        },
        animationSpec = tween(ArcaneMotion.Fast),
        label = "tabContentColor"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (tab.enabled && selected && isFilled) 0.3f else 0f,
        animationSpec = tween(ArcaneMotion.Fast),
        label = "tabGlowAlpha"
    )

    Box(
        modifier = modifier
            .height(40.dp)
            // Glow effect using arcaneGlow modifier
            .arcaneGlowIf(
                enabled = tab.enabled && isFilled,
                color = colors.glow,
                alpha = glowAlpha,
                radiusFactor = 0.8f
            )
            .clip(ArcaneRadius.Medium)
            .background(backgroundColor, ArcaneRadius.Medium)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = tab.enabled,
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = ArcaneSpacing.Medium),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tab.icon?.let { icon ->
                    Box(modifier = Modifier.size(ArcaneIconography.Small)) {
                        icon()
                    }
                }
                Text(
                    text = tab.label,
                    style = ArcaneTheme.typography.labelLarge,
                    color = LocalContentColor.current
                )
            }
        }
    }
}

@Composable
fun ArcaneTabs(
    tabs: List<ArcaneTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: ArcaneTabStyle = ArcaneTabStyle.Filled,
    scrollable: Boolean = false
) {
    if (scrollable) {
        Row(
            modifier = modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                ArcaneTabItem(
                    tab = tab,
                    selected = index == selectedIndex,
                    onClick = { if (tab.enabled) onTabSelected(index) },
                    style = style
                )
            }
        }
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                ArcaneTabItem(
                    tab = tab,
                    selected = index == selectedIndex,
                    onClick = { if (tab.enabled) onTabSelected(index) },
                    style = style,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ArcaneTabLayout(
    tabs: List<ArcaneTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: ArcaneTabStyle = ArcaneTabStyle.Filled,
    scrollable: Boolean = false,
    content: @Composable (selectedIndex: Int) -> Unit
) {
    Column(modifier = modifier) {
        ArcaneTabs(
            tabs = tabs,
            selectedIndex = selectedIndex,
            onTabSelected = onTabSelected,
            modifier = Modifier.fillMaxWidth(),
            style = style,
            scrollable = scrollable
        )
        content(selectedIndex)
    }
}
