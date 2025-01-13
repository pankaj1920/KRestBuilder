package com.app.krestbuilder.remote.curl.extractor

fun getBody(curl: String): String {
    val bodyRegex = Regex("--data\\s+'(\\{.*?})'", RegexOption.DOT_MATCHES_ALL)
    val bodyMatch = bodyRegex.find(curl)
    return bodyMatch?.groups?.get(1)?.value ?: "{}" // Default to empty JSON if no body is found
}
