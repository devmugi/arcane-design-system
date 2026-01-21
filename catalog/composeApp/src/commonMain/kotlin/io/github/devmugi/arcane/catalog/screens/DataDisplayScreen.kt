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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneTextButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.components.display.ArcaneAvatar
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarData
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarGroup
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarSize
import io.github.devmugi.arcane.design.components.display.ArcaneBadge
import io.github.devmugi.arcane.design.components.display.ArcaneBadgeStyle
import io.github.devmugi.arcane.design.components.display.ArcaneCard
import io.github.devmugi.arcane.design.components.display.ArcaneCardActions
import io.github.devmugi.arcane.design.components.display.ArcaneCardContent
import io.github.devmugi.arcane.design.components.display.ArcaneListItem
import io.github.devmugi.arcane.design.components.display.ArcaneTable
import io.github.devmugi.arcane.design.components.display.ArcaneTableColumn
import io.github.devmugi.arcane.design.components.display.ArcaneTableSortState
import io.github.devmugi.arcane.design.components.display.ArcaneTooltip
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun DataDisplayScreen(onBack: () -> Unit = {}) {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = colors.primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )
            Text(
                text = "Data Display",
                style = typography.displayMedium,
                color = colors.text
            )
        }

        // Badges Section
        SectionTitle("Badges")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneBadge("New", style = ArcaneBadgeStyle.Success)
                ArcaneBadge("Featured", style = ArcaneBadgeStyle.Default)
                ArcaneBadge("Sale", style = ArcaneBadgeStyle.Warning)
                ArcaneBadge("Error", style = ArcaneBadgeStyle.Error)
                ArcaneBadge("Neutral", style = ArcaneBadgeStyle.Neutral)
            }
        }

        // Avatars Section
        SectionTitle("Avatars")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                Text("Sizes", style = typography.labelMedium, color = colors.textSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
                    ArcaneAvatar(name = "John Doe", size = ArcaneAvatarSize.Small)
                    ArcaneAvatar(name = "Jane Smith", size = ArcaneAvatarSize.Medium)
                    ArcaneAvatar(name = "Bob Wilson", size = ArcaneAvatarSize.Large)
                }

                Text("Avatar Group", style = typography.labelMedium, color = colors.textSecondary)
                ArcaneAvatarGroup(
                    avatars = listOf(
                        ArcaneAvatarData(name = "Alice"),
                        ArcaneAvatarData(name = "Bob"),
                        ArcaneAvatarData(name = "Charlie"),
                        ArcaneAvatarData(name = "Diana"),
                        ArcaneAvatarData(name = "Eve")
                    ),
                    maxVisible = 3
                )
            }
        }

        // List Items Section
        SectionTitle("List Items")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ArcaneListItem(
                    headlineText = "Meeting Tomorrow",
                    supportingText = "10:00 AM - 11:00 AM, Room A"
                )
                ArcaneListItem(
                    headlineText = "Project Review",
                    supportingText = "2:00 PM - 3:00 PM, Virtual",
                    trailingContent = {
                        ArcaneBadge("New", style = ArcaneBadgeStyle.Success)
                    }
                )
                ArcaneListItem(
                    headlineText = "Team Standup",
                    supportingText = "9:00 AM - 9:15 AM, Daily",
                    onClick = { }
                )
            }
        }

        // Cards Section
        SectionTitle("Cards")
        ArcaneCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            ArcaneCardContent(
                title = "Project Phoenix",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            )
            ArcaneCardActions {
                ArcaneTextButton(
                    text = "View Project",
                    onClick = { },
                    style = ArcaneButtonStyle.Secondary
                )
            }
        }

        // Tooltip Section
        SectionTitle("Tooltip")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                ArcaneTooltip(text = "This is helpful information") {
                    ArcaneTextButton(
                        text = "Hover me",
                        onClick = { },
                        style = ArcaneButtonStyle.Secondary
                    )
                }
            }
        }

        // Table Section
        SectionTitle("Table")
        var sortState by remember { mutableStateOf<ArcaneTableSortState?>(null) }

        data class TableItem(val name: String, val status: String, val date: String)
        val items = listOf(
            TableItem("Project Alpha", "Active", "Jan 15"),
            TableItem("Project Beta", "Pending", "Jan 18"),
            TableItem("Project Gamma", "Complete", "Jan 20")
        )

        ArcaneTable(
            items = items,
            columns = listOf(
                ArcaneTableColumn(
                    header = "Name",
                    weight = 1.5f,
                    sortable = true
                ) { item ->
                    Text(item.name, style = typography.bodyMedium, color = colors.text)
                },
                ArcaneTableColumn(
                    header = "Status",
                    filterable = true
                ) { item ->
                    val badgeStyle = when (item.status) {
                        "Active" -> ArcaneBadgeStyle.Success
                        "Pending" -> ArcaneBadgeStyle.Warning
                        else -> ArcaneBadgeStyle.Neutral
                    }
                    ArcaneBadge(item.status, style = badgeStyle)
                },
                ArcaneTableColumn(
                    header = "Date",
                    sortable = true
                ) { item ->
                    Text(item.date, style = typography.bodyMedium, color = colors.textSecondary)
                }
            ),
            sortState = sortState,
            onSortChange = { sortState = it },
            modifier = Modifier.fillMaxWidth()
        )

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
