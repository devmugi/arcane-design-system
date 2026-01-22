package io.github.devmugi.arcane.catalog.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Standard component preview wrapper for catalog-chat screens.
 *
 * Wraps content in a device frame (when deviceType != None) with:
 * - Device bezel and border
 * - Status bar with system icons
 * - Navigation bar with home indicator
 * - Notch or Dynamic Island overlay
 * - Safe area padding to prevent content overlap with chrome
 *
 * @param deviceType The device frame to display (None, Pixel8, iPhone16)
 * @param modifier Modifier for the preview container
 * @param content The component content to display inside the device frame
 */
@Composable
fun ComponentPreview(
    deviceType: DeviceType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (deviceType == DeviceType.None) {
        // No preview frame
        content()
    } else {
        // Wrap in device frame with chrome
        DevicePreview(
            deviceType = deviceType,
            modifier = modifier
        ) {
            // Apply safe area padding
            val safeInsets = deviceType.getSafeAreaInsets()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = safeInsets.top,
                        bottom = safeInsets.bottom,
                        start = safeInsets.left,
                        end = safeInsets.right
                    )
            ) {
                content()
            }
        }
    }
}
