// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ControlsScreen.kt
package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun ControlsScreen() {
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
        Text(
            text = "Controls",
            style = typography.displayMedium,
            color = colors.text
        )

        // Buttons Section
        SectionTitle("Buttons")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneTextButton(
                    text = "Primary",
                    onClick = {},
                    style = ArcaneButtonStyle.Primary
                )
                ArcaneTextButton(
                    text = "Secondary",
                    onClick = {},
                    style = ArcaneButtonStyle.Secondary
                )
                ArcaneTextButton(
                    text = "Loading",
                    onClick = {},
                    loading = true
                )
                ArcaneTextButton(
                    text = "Disabled",
                    onClick = {},
                    enabled = false
                )
                ArcaneTextButton(
                    text = "Outlined",
                    onClick = {},
                    style = ArcaneButtonStyle.Outlined()
                )
                ArcaneTextButton(
                    text = "Outlined Gold",
                    onClick = {},
                    style = ArcaneButtonStyle.Outlined(tintColor = Color(0xFFD4A574))
                )
            }
        }

        // TextField Section
        SectionTitle("Text Field")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
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
            variant = SurfaceVariant.Raised,
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
            variant = SurfaceVariant.Raised,
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
            variant = SurfaceVariant.Raised,
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
