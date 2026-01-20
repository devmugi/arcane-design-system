package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
object ArcaneRadius {
    /** No rounding */
    val None = RoundedCornerShape(0.dp)

    /** B4 - 4px subtle rounding */
    val Small = RoundedCornerShape(4.dp)

    /** B8 - 6px (labeled as B8 but 6px per design) */
    val Medium = RoundedCornerShape(6.dp)

    /** R12 - 12px standard rounding */
    val Large = RoundedCornerShape(12.dp)

    /** R15 - 18px (labeled R15 but 18px per design) */
    val ExtraLarge = RoundedCornerShape(18.dp)

    /** Full pill shape */
    val Full = RoundedCornerShape(50)
}
