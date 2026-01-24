// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ControlsScreen.kt
package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonShape
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonSize
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.components.controls.ArcaneCheckbox
import io.github.devmugi.arcane.design.components.controls.ArcaneRadioButton
import io.github.devmugi.arcane.design.components.controls.ArcaneSlider
import io.github.devmugi.arcane.design.components.controls.ArcaneSwitch
import io.github.devmugi.arcane.design.components.controls.ArcaneTextButton
import io.github.devmugi.arcane.design.components.controls.ArcaneTextField
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ControlsScreen(windowSizeClass: WindowSizeClass? = null) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {

        // Buttons Section - M3 Types
        SectionTitle("Buttons - Material 3 Types")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                Text(
                    text = "Filled (highest emphasis)",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Filled",
                    onClick = {},
                    style = ArcaneButtonStyle.Filled()
                )

                Text(
                    text = "Tonal (medium emphasis)",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Tonal",
                    onClick = {},
                    style = ArcaneButtonStyle.Tonal()
                )

                Text(
                    text = "Outlined (medium emphasis)",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Outlined",
                    onClick = {},
                    style = ArcaneButtonStyle.Outlined()
                )

                Text(
                    text = "Elevated (medium emphasis)",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Elevated",
                    onClick = {},
                    style = ArcaneButtonStyle.Elevated()
                )

                Text(
                    text = "Text (lowest emphasis)",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Text Button",
                    onClick = {},
                    style = ArcaneButtonStyle.Text
                )
            }
        }

        // Button States
        SectionTitle("Button States")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneTextButton(
                    text = "Loading (Filled)",
                    onClick = {},
                    loading = true,
                    style = ArcaneButtonStyle.Filled()
                )
                ArcaneTextButton(
                    text = "Loading (Tonal)",
                    onClick = {},
                    loading = true,
                    style = ArcaneButtonStyle.Tonal()
                )
                ArcaneTextButton(
                    text = "Loading (Outlined)",
                    onClick = {},
                    loading = true,
                    style = ArcaneButtonStyle.Outlined()
                )
                ArcaneTextButton(
                    text = "Disabled",
                    onClick = {},
                    enabled = false
                )
                ArcaneTextButton(
                    text = "Destructive",
                    onClick = {},
                    style = ArcaneButtonStyle.Filled(containerColor = colors.error)
                )
            }
        }

        // Button Sizes
        SectionTitle("Button Sizes")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                Text(
                    text = "Medium (40dp) - Default",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Medium Button",
                    onClick = {},
                    size = ArcaneButtonSize.Medium
                )

                Text(
                    text = "Small (32dp)",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Small Button",
                    onClick = {},
                    size = ArcaneButtonSize.Small
                )

                Text(
                    text = "Extra Small (24dp)",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Extra Small",
                    onClick = {},
                    size = ArcaneButtonSize.ExtraSmall
                )
            }
        }

        // Button Shapes
        SectionTitle("Button Shapes")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                Text(
                    text = "Round (full pill) - Default",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Round Button",
                    onClick = {},
                    shape = ArcaneButtonShape.Round
                )

                Text(
                    text = "Rounded (20dp radius)",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Rounded Button",
                    onClick = {},
                    shape = ArcaneButtonShape.Rounded
                )

                Text(
                    text = "Square (0dp radius)",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                ArcaneTextButton(
                    text = "Square Button",
                    onClick = {},
                    shape = ArcaneButtonShape.Square
                )
            }
        }

        // Button Combinations
        SectionTitle("Button Combinations")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    ArcaneTextButton(
                        text = "Small Rounded",
                        onClick = {},
                        size = ArcaneButtonSize.Small,
                        shape = ArcaneButtonShape.Rounded,
                        style = ArcaneButtonStyle.Filled()
                    )
                    ArcaneTextButton(
                        text = "Small Square",
                        onClick = {},
                        size = ArcaneButtonSize.Small,
                        shape = ArcaneButtonShape.Square,
                        style = ArcaneButtonStyle.Outlined()
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    ArcaneTextButton(
                        text = "XS",
                        onClick = {},
                        size = ArcaneButtonSize.ExtraSmall,
                        shape = ArcaneButtonShape.Rounded,
                        style = ArcaneButtonStyle.Tonal()
                    )
                    ArcaneTextButton(
                        text = "XS Elevated",
                        onClick = {},
                        size = ArcaneButtonSize.ExtraSmall,
                        shape = ArcaneButtonShape.Rounded,
                        style = ArcaneButtonStyle.Elevated()
                    )
                }
            }
        }

        // TextField Section
        SectionTitle("Text Field")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                var text1 by remember { mutableStateOf("") }
                ArcaneTextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    placeholder = "Placeholder"
                )

                var text2 by remember { mutableStateOf("") }
                ArcaneTextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    label = "Helper Text",
                    helperText = "Password must be 8+ characters"
                )

                var text3 by remember { mutableStateOf("") }
                ArcaneTextField(
                    value = text3,
                    onValueChange = { text3 = it },
                    label = "Focused Input Highlight",
                    placeholder = "Enter text..."
                )

                ArcaneTextField(
                    value = "Invalid email format",
                    onValueChange = {},
                    label = "Error State",
                    errorText = "Invalid email format"
                )
            }
        }

        // Tactile Section (Checkbox & Radio)
        SectionTitle("Tactile")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                var checked by remember { mutableStateOf(true) }
                ArcaneCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    label = "Checkbox"
                )

                var selectedRadio by remember { mutableStateOf(0) }
                ArcaneRadioButton(
                    selected = selectedRadio == 0,
                    onClick = { selectedRadio = 0 },
                    label = "Radio Button"
                )
                ArcaneRadioButton(
                    selected = selectedRadio == 1,
                    onClick = { selectedRadio = 1 },
                    label = "OFF"
                )
            }
        }

        // Switch Section
        SectionTitle("Switch")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
            ) {
                var switchOn by remember { mutableStateOf(true) }
                ArcaneSwitch(
                    checked = switchOn,
                    onCheckedChange = { switchOn = it },
                    label = "ON"
                )
                ArcaneSwitch(
                    checked = false,
                    onCheckedChange = {},
                    label = "OFF"
                )
            }
        }

        // Slider Section
        SectionTitle("Slider")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium)
            ) {
                var sliderValue by remember { mutableStateOf(0.5f) }
                ArcaneSlider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = ArcaneTheme.typography.headlineLarge,
        color = ArcaneTheme.colors.textSecondary
    )
}
