package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

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
