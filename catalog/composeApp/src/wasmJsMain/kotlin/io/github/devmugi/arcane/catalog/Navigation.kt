package io.github.devmugi.arcane.catalog

import kotlinx.browser.window

actual fun navigateToFullCatalog() {
    window.location.href = "../catalog/"
}
