package io.github.devmugi.arcane.catalog.chat.screens

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal actual fun getCurrentTimestamp(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = now.hour
    val minute = now.minute
    val formattedHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    val amPm = if (hour < 12) "AM" else "PM"
    val minuteStr = if (minute < 10) "0$minute" else "$minute"
    return "$formattedHour:$minuteStr $amPm"
}
