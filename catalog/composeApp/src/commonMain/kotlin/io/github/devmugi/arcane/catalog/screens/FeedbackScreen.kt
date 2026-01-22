package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.components.controls.ArcaneTextButton
import io.github.devmugi.arcane.design.components.feedback.ArcaneAlertAction
import io.github.devmugi.arcane.design.components.feedback.ArcaneAlertBanner
import io.github.devmugi.arcane.design.components.feedback.ArcaneAlertStyle
import io.github.devmugi.arcane.design.components.feedback.ArcaneCircularProgress
import io.github.devmugi.arcane.design.components.feedback.ArcaneConfirmationDialog
import io.github.devmugi.arcane.design.components.feedback.ArcaneConfirmationStyle
import io.github.devmugi.arcane.design.components.feedback.ArcaneEmptyState
import io.github.devmugi.arcane.design.components.feedback.ArcaneLinearProgress
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeletonCard
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeletonListItem
import io.github.devmugi.arcane.design.components.feedback.ArcaneSpinner
import io.github.devmugi.arcane.design.components.feedback.ArcaneSpinnerSize
import io.github.devmugi.arcane.design.components.feedback.ArcaneToastHost
import io.github.devmugi.arcane.design.components.feedback.ArcaneToastPosition
import io.github.devmugi.arcane.design.components.feedback.ArcaneToastStyle
import io.github.devmugi.arcane.design.components.feedback.rememberArcaneToastState
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun FeedbackScreen() {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors
    val scrollState = rememberScrollState()
    val toastState = rememberArcaneToastState()

    var showDefaultDialog by remember { mutableStateOf(false) }
    var showDestructiveDialog by remember { mutableStateOf(false) }

    // Confirmation Dialogs
    ArcaneConfirmationDialog(
        visible = showDefaultDialog,
        onDismiss = { showDefaultDialog = false },
        onConfirm = { toastState.show("Confirmed!", ArcaneToastStyle.Success) },
        title = "Confirm Action",
        description = "Are you sure you want to proceed with this action?"
    )

    ArcaneConfirmationDialog(
        visible = showDestructiveDialog,
        onDismiss = { showDestructiveDialog = false },
        onConfirm = { toastState.show("Item deleted", ArcaneToastStyle.Error) },
        title = "Delete Item?",
        description = "Are you sure you want to delete this item?",
        confirmText = "Delete",
        cancelText = "Cancel",
        style = ArcaneConfirmationStyle.Destructive
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(ArcaneSpacing.Medium),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
        ) {
            // Modals Section
        SectionTitle("Modals")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneTextButton(
                    text = "Confirmation",
                    onClick = { showDefaultDialog = true },
                    style = ArcaneButtonStyle.Tonal()
                )
                ArcaneTextButton(
                    text = "Destructive",
                    onClick = { showDestructiveDialog = true },
                    style = ArcaneButtonStyle.Tonal()
                )
            }
        }

        // Toasts Section
        SectionTitle("Toasts")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneTextButton(
                    text = "Default",
                    onClick = { toastState.show("This is a default toast") },
                    style = ArcaneButtonStyle.Tonal()
                )
                ArcaneTextButton(
                    text = "Success",
                    onClick = { toastState.show("Operation successful!", ArcaneToastStyle.Success) },
                    style = ArcaneButtonStyle.Tonal()
                )
                ArcaneTextButton(
                    text = "Warning",
                    onClick = { toastState.show("Please review your input", ArcaneToastStyle.Warning) },
                    style = ArcaneButtonStyle.Tonal()
                )
                ArcaneTextButton(
                    text = "Error",
                    onClick = { toastState.show("Something went wrong", ArcaneToastStyle.Error) },
                    style = ArcaneButtonStyle.Tonal()
                )
            }
        }

        // Alert Banners Section
        SectionTitle("Alert Banners")
        Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
            ArcaneAlertBanner(
                message = "This is an informational message.",
                style = ArcaneAlertStyle.Info,
                onDismiss = { }
            )
            ArcaneAlertBanner(
                message = "Operation completed successfully!",
                style = ArcaneAlertStyle.Success
            )
            ArcaneAlertBanner(
                message = "Server is experiencing high load.",
                style = ArcaneAlertStyle.Warning,
                action = ArcaneAlertAction("Retry") { }
            )
            ArcaneAlertBanner(
                message = "Connection failed. Please try again.",
                style = ArcaneAlertStyle.Error,
                onDismiss = { },
                action = ArcaneAlertAction("Retry") { }
            )
        }

        // Progress Section
        SectionTitle("Progress")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                Text("Circular Progress", style = typography.labelMedium, color = colors.textSecondary)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArcaneCircularProgress(progress = 0.25f)
                    ArcaneCircularProgress(progress = 0.5f, showLabel = true)
                    ArcaneCircularProgress(progress = 0.75f, size = 64.dp, showLabel = true)
                }

                Text("Linear Progress", style = typography.labelMedium, color = colors.textSecondary)
                Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
                    ArcaneLinearProgress(progress = 0.3f)
                    ArcaneLinearProgress(progress = 0.6f)
                    ArcaneLinearProgress(progress = 0.9f)
                }
            }
        }

        // Spinner Section
        SectionTitle("Spinner")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ArcaneSpinner(size = ArcaneSpinnerSize.Small)
                    Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
                    Text("Small", style = typography.labelSmall, color = colors.textSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ArcaneSpinner(size = ArcaneSpinnerSize.Medium)
                    Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
                    Text("Medium", style = typography.labelSmall, color = colors.textSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ArcaneSpinner(size = ArcaneSpinnerSize.Large)
                    Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
                    Text("Large", style = typography.labelSmall, color = colors.textSecondary)
                }
            }
        }

        // Skeletons Section
        SectionTitle("Skeletons")
        ArcaneSurface(
            variant = SurfaceVariant.Container,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                Text("List Item Skeleton", style = typography.labelMedium, color = colors.textSecondary)
                ArcaneSkeletonListItem()
                ArcaneSkeletonListItem(showTrailingContent = true)

                Text("Card Skeleton", style = typography.labelMedium, color = colors.textSecondary)
                ArcaneSkeletonCard(modifier = Modifier.fillMaxWidth())
            }
        }

        // Empty State Section
        SectionTitle("Empty State")
        ArcaneEmptyState {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = colors.textDisabled
            )
            Spacer(modifier = Modifier.height(ArcaneSpacing.Medium))
            Text(
                text = "No items found",
                style = typography.headlineMedium,
                color = colors.text
            )
            Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
            Text(
                text = "Start by adding a new project.",
                style = typography.bodyMedium,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(ArcaneSpacing.Large))
            ArcaneButton(onClick = { }) {
                Text("Add Project")
            }
        }

            Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
        }

        // Toast Host as overlay
        ArcaneToastHost(
            state = toastState,
            position = ArcaneToastPosition.BottomCenter
        )
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
