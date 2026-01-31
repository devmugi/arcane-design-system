package io.github.devmugi.arcane.chat.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File

/**
 * Captures a screenshot of the given chat composable content wrapped in ArcaneTheme.
 *
 * @param componentName The name of the component (used in the file path)
 * @param isDark Whether to use dark theme
 * @param width Width of the capture area in pixels
 * @param height Height of the capture area in pixels
 * @param content The composable content to capture
 */
@OptIn(ExperimentalTestApi::class)
fun captureChatDesktopScreenshot(
    category: String,
    componentName: String,
    isDark: Boolean = false,
    width: Int = 400,
    height: Int = 200,
    content: @Composable () -> Unit
) {
    val theme = if (isDark) "dark" else "light"
    val goldenPath = "src/desktopTest/resources/golden/desktop/$theme/$category/${componentName}.png"
    val goldenFile = File(goldenPath)

    runDesktopComposeUiTest(width = width, height = height) {
        setContent {
            ArcaneTheme(
                isDark = isDark,
                colors = if (isDark) ArcaneColors.dark() else ArcaneColors.default()
            ) {
                content()
            }
        }

        waitForIdle()

        val bitmap = onRoot().captureToImage()
        val skiaBitmap = bitmap.asSkiaBitmap()
        val skiaImage = Image.makeFromBitmap(skiaBitmap)
        val pngData = skiaImage.encodeToData(EncodedImageFormat.PNG)

        if (pngData != null) {
            goldenFile.parentFile?.mkdirs()
            goldenFile.writeBytes(pngData.bytes)
        }
    }
}
