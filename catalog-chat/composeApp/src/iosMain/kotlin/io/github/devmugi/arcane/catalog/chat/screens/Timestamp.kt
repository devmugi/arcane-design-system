package io.github.devmugi.arcane.catalog.chat.screens

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

internal actual fun getCurrentTimestamp(): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "h:mm a"
    return formatter.stringFromDate(NSDate())
}
