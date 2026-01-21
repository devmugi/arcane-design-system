package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
object ArcaneRadius {
    /** R0 - No rounding */
    val None = RoundedCornerShape(0.dp)

    /** R4 - 4px subtle rounding */
    val Small = RoundedCornerShape(4.dp)

    /** R8 - 8px standard rounding */
    val Medium = RoundedCornerShape(8.dp)

    /** R12 - 12px moderate rounding */
    val Large = RoundedCornerShape(12.dp)

    /** R15 - 16px large rounding */
    val ExtraLarge = RoundedCornerShape(16.dp)

    /** Full pill shape */
    val Full = RoundedCornerShape(50)
}
