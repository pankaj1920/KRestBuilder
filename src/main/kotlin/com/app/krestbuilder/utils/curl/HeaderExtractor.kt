package com.app.krestbuilder.utils.curl

import com.app.krestbuilder.utils.json.mapToJson

fun getHeaderString(curl: String): String {
    val headerRegex = Regex("--header\\s+'([^:]+):\\s*(.+?)'")
    val headers = mutableMapOf<String, String>()

    headerRegex.findAll(curl).forEach { matchResult ->
        val key = matchResult.groups[1]?.value ?: ""
        val value = matchResult.groups[2]?.value ?: ""
        headers[key] = value
    }

    return headers.mapToJson()
}

fun getHeaderMap(curl: String): Map<String, String> {
    val headerRegex = Regex("--header\\s+'([^:]+):\\s*(.+?)'")
    val headers = mutableMapOf<String, String>()

    headerRegex.findAll(curl).forEach { matchResult ->
        val key = matchResult.groups[1]?.value ?: ""
        val value = matchResult.groups[2]?.value ?: ""
        headers[key] = value
    }

    // Debugging to see if headers are captured correctly
    println("Headers: $headers")

    return headers
}