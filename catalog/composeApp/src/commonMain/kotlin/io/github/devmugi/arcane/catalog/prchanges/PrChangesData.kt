package io.github.devmugi.arcane.catalog.prchanges

import kotlinx.serialization.Serializable

@Serializable
data class PrChangesManifest(
    val prNumber: Int = 0,
    val baseBranch: String = "main",
    val changedComponents: List<ChangedComponent> = emptyList(),
    val affectedScreens: List<String> = emptyList(),
    val generatedAt: String = ""
)

@Serializable
data class ChangedComponent(
    val name: String,
    val screen: String,
    val changeType: String, // "added", "modified", "deleted"
    val filePath: String
)
