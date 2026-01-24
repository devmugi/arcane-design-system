package io.github.devmugi.arcane.catalog.chat.screens

private fun getJsTimestamp(): String = js("""
(function() {
    var now = new Date();
    var hours = now.getHours();
    var minutes = now.getMinutes();
    var ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    return hours + ':' + minutes + ' ' + ampm;
})()
""")

internal actual fun getCurrentTimestamp(): String = getJsTimestamp()
