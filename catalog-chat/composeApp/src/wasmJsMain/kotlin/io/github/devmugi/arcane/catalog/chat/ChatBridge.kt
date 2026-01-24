@file:OptIn(kotlin.js.ExperimentalJsExport::class)
package io.github.devmugi.arcane.catalog.chat

/**
 * JavaScript bridge for browser automation tools.
 *
 * The exported functions are available through the WASM module exports.
 * After the app loads, use:
 * - window.arcaneChat.sendMessage("text") - Send a message and get fake response
 * - window.arcaneChat.setInput("text") - Set input field text
 * - window.arcaneChat.getMessageCount() - Get current message count
 * - window.arcaneChat.clearMessages() - Clear all messages
 */

// Top-level exported functions - these get exported with the WASM module
@JsExport
fun arcaneChatSendMessage(text: String) {
    ChatController.sendMessage(text)
}

@JsExport
fun arcaneChatSetInput(text: String) {
    ChatController.setInput(text)
}

@JsExport
fun arcaneChatGetMessageCount(): Int {
    return ChatController.messages.size
}

@JsExport
fun arcaneChatClearMessages() {
    ChatController.clearMessages()
}

fun setupChatBridge() {
    // The bridge setup happens in index.html after WASM loads
    // This function can be used for any Kotlin-side initialization
}
