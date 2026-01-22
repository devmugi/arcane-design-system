package io.github.devmugi.arcane.catalog.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

data class SafeAreaInsets(
    val top: Dp,
    val bottom: Dp,
    val left: Dp = 0.dp,
    val right: Dp = 0.dp
)

fun DeviceType.getSafeAreaInsets(): SafeAreaInsets {
    return when (this) {
        DeviceType.None -> SafeAreaInsets(top = 0.dp, bottom = 0.dp)
        DeviceType.Pixel8 -> SafeAreaInsets(
            top = 60.dp,  // Status bar (24) + punch hole position (24) + punch hole diameter (12)
            bottom = 16.dp // Gesture bar
        )
        DeviceType.iPhone16 -> SafeAreaInsets(
            top = 81.dp,  // Status bar (24) + Dynamic Island position (20) + island height (37)
            bottom = 34.dp // Home indicator
        )
    }
}

@Composable
fun StatusBar(
    deviceType: DeviceType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .background(Color.Black.copy(alpha = 0.9f))
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time
            Text(
                text = "9:41",
                style = ArcaneTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 11.sp
            )

            // Status icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.SignalCellularAlt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Icon(
                    imageVector = Icons.Default.BatteryFull,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun NavigationBar(
    deviceType: DeviceType,
    modifier: Modifier = Modifier
) {
    val height = when (deviceType) {
        DeviceType.Pixel8 -> 16.dp
        DeviceType.iPhone16 -> 34.dp
        else -> 0.dp
    }

    if (height > 0.dp) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .background(Color.Black.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            // Home indicator
            Box(
                modifier = Modifier
                    .width(if (deviceType == DeviceType.iPhone16) 134.dp else 100.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.6f))
            )
        }
    }
}
