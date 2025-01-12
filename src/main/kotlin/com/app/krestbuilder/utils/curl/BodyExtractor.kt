package com.app.krestbuilder.utils.curl

fun getBody(curl: String): String {
    val bodyRegex = Regex("--data\\s+'(\\{.*?})'", RegexOption.DOT_MATCHES_ALL)
    val bodyMatch = bodyRegex.find(curl)
    return bodyMatch?.groups?.get(1)?.value ?: "{}" // Default to empty JSON if no body is found
}
