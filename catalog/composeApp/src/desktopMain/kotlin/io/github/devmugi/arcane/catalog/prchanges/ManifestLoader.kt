package io.github.devmugi.arcane.catalog.prchanges

actual suspend fun loadPrChangesManifest(): PrChangesManifest? {
    // Desktop doesn't use PR changes filtering
    return null
}
