package io.github.devmugi.arcane.catalog.prchanges

actual suspend fun loadPrChangesManifest(): PrChangesManifest? {
    // PR preview not supported on iOS app
    return null
}
