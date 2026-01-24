package io.github.devmugi.arcane.catalog.prchanges

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object PrChangesConfig {
    var manifest: PrChangesManifest by mutableStateOf(PrChangesManifest())
        private set

    val isFilteredMode: Boolean
        get() = manifest.affectedScreens.isNotEmpty()

    val changedComponentNames: Set<String>
        get() = manifest.changedComponents.map { it.name }.toSet()

    val affectedScreens: List<String>
        get() = manifest.affectedScreens

    fun loadManifest(manifest: PrChangesManifest) {
        this.manifest = manifest
    }

    fun isComponentChanged(componentName: String): Boolean {
        return componentName in changedComponentNames
    }

    fun isScreenAffected(screenName: String): Boolean {
        return screenName in affectedScreens
    }
}
