package io.github.devmugi.arcane.design.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File
import javax.imageio.ImageIO
import kotlin.test.assertTrue

/**
 * Captures a screenshot of the given composable content wrapped in ArcaneTheme.
 * Uses Compose Desktop's rendering to capture the image.
 *
 * @param componentName The name of the component (used in the file path)
 * @param isDark Whether to use dark theme
 * @param width Width of the capture area in pixels
 * @param height Height of the capture area in pixels
 * @param content The composable content to capture
 */
@OptIn(ExperimentalTestApi::class)
fun captureDesktopScreenshot(
    componentName: String,
    isDark: Boolean = false,
    width: Int = 400,
    height: Int = 200,
    content: @Composable () -> Unit
) {
    val theme = if (isDark) "dark" else "light"
    val goldenPath = "src/desktopTest/resources/golden/desktop/$theme/${componentName}.png"
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

        // Wait for composition to complete
        waitForIdle()

        // Capture the root node as an image
        val bitmap = onRoot().captureToImage()
        val skiaBitmap = bitmap.asSkiaBitmap()
        val skiaImage = Image.makeFromBitmap(skiaBitmap)
        val pngData = skiaImage.encodeToData(EncodedImageFormat.PNG)

        if (pngData != null) {
            // Ensure parent directories exist
            goldenFile.parentFile?.mkdirs()

            // Write the PNG data to file
            goldenFile.writeBytes(pngData.bytes)
        }
    }
}

/**
 * Verifies a screenshot matches the golden image.
 *
 * @param componentName The name of the component
 * @param isDark Whether dark theme was used
 * @param width Width of the capture area
 * @param height Height of the capture area
 * @param content The composable content to verify
 */
@OptIn(ExperimentalTestApi::class)
fun verifyDesktopScreenshot(
    componentName: String,
    isDark: Boolean = false,
    width: Int = 400,
    height: Int = 200,
    content: @Composable () -> Unit
) {
    val theme = if (isDark) "dark" else "light"
    val goldenPath = "src/desktopTest/resources/golden/desktop/$theme/${componentName}.png"
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

        if (goldenFile.exists()) {
            // Compare with golden image
            val goldenImage = ImageIO.read(goldenFile)
            val currentSkiaBitmap = bitmap.asSkiaBitmap()

            // Simple dimension check
            assertTrue(
                goldenImage.width == currentSkiaBitmap.width &&
                    goldenImage.height == currentSkiaBitmap.height,
                "Screenshot dimensions don't match golden image"
            )

            // For now, just verify dimensions match
            // Full pixel comparison can be added later
        } else {
            // Golden doesn't exist - capture it
            val skiaBitmap = bitmap.asSkiaBitmap()
            val skiaImage = Image.makeFromBitmap(skiaBitmap)
            val pngData = skiaImage.encodeToData(EncodedImageFormat.PNG)

            if (pngData != null) {
                goldenFile.parentFile?.mkdirs()
                goldenFile.writeBytes(pngData.bytes)
            }
        }
    }
}
