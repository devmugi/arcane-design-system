@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package io.github.devmugi.arcane.catalog.prchanges

import kotlinx.browser.window
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val json = Json { ignoreUnknownKeys = true }

actual suspend fun loadPrChangesManifest(): PrChangesManifest? {
    return try {
        val response = fetchText("pr-changes.json")
        if (response != null) {
            json.decodeFromString<PrChangesManifest>(response)
        } else {
            null
        }
    } catch (e: Exception) {
        console.log("PR changes manifest not found or invalid: ${e.message}")
        null
    }
}

private suspend fun fetchText(url: String): String? = suspendCoroutine { continuation ->
    window.fetch(url).then { response ->
        if (response.ok) {
            response.text().then { jsText ->
                continuation.resume(jsText.toString())
                null
            }.catch { error ->
                continuation.resumeWithException(Exception("Failed to read response: $error"))
                null
            }
        } else {
            continuation.resume(null)
        }
        null
    }.catch { error ->
        continuation.resumeWithException(Exception("Fetch failed: $error"))
        null
    }
}

private external val console: Console

private external interface Console {
    fun log(message: String)
}
