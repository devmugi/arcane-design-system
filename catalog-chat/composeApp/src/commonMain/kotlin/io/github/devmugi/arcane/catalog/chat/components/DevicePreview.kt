package io.github.devmugi.arcane.catalog.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant

enum class DeviceType {
    None,
    Pixel8,
    iPhone16;

    val displayName: String
        get() = when (this) {
            None -> "No Preview"
            Pixel8 -> "Pixel 8"
            iPhone16 -> "iPhone 16"
        }
}

sealed class NotchType {
    data class PunchHole(
        val centerX: Dp,
        val y: Dp,
        val radius: Dp = 6.dp
    ) : NotchType()

    data class DynamicIsland(
        val centerX: Dp,
        val y: Dp,
        val width: Dp = 126.dp,
        val height: Dp = 37.dp
    ) : NotchType()
}

@Composable
fun DevicePreview(
    deviceType: DeviceType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    when (deviceType) {
        DeviceType.None -> content()
        DeviceType.Pixel8 -> DeviceFrame(
            screenWidth = 412.dp,
            screenHeight = 915.dp,
            cornerRadius = 32.dp,
            notchType = NotchType.PunchHole(centerX = 206.dp, y = 24.dp),
            modifier = modifier,
            content = content
        )
        DeviceType.iPhone16 -> DeviceFrame(
            screenWidth = 393.dp,
            screenHeight = 852.dp,
            cornerRadius = 55.dp,
            notchType = NotchType.DynamicIsland(centerX = 196.5.dp, y = 20.dp),
            modifier = modifier,
            content = content
        )
    }
}

@Composable
private fun DeviceFrame(
    screenWidth: Dp,
    screenHeight: Dp,
    cornerRadius: Dp,
    notchType: NotchType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Device bezel with shadow
        ArcaneSurface(
            variant = SurfaceVariant.ContainerHigh,
            modifier = Modifier
                .size(width = screenWidth + 16.dp, height = screenHeight + 16.dp)
                .clip(RoundedCornerShape(cornerRadius + 4.dp))
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(cornerRadius + 4.dp)
                )
        ) {
            // Screen content area
            Box(
                modifier = Modifier
                    .size(width = screenWidth, height = screenHeight)
                    .clip(RoundedCornerShape(cornerRadius))
                    .align(Alignment.Center)
            ) {
                content()

                // Notch overlay
                when (notchType) {
                    is NotchType.PunchHole -> {
                        Box(
                            modifier = Modifier
                                .size(notchType.radius * 2)
                                .align(Alignment.TopCenter)
                                .offset(x = notchType.centerX - screenWidth / 2, y = notchType.y)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                        )
                    }
                    is NotchType.DynamicIsland -> {
                        Box(
                            modifier = Modifier
                                .size(width = notchType.width, height = notchType.height)
                                .align(Alignment.TopCenter)
                                .offset(
                                    x = notchType.centerX - screenWidth / 2,
                                    y = notchType.y
                                )
                                .clip(RoundedCornerShape(notchType.height / 2))
                                .background(Color.Black)
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.2f),
                                    RoundedCornerShape(notchType.height / 2)
                                )
                        )
                    }
                }
            }
        }
    }
}
