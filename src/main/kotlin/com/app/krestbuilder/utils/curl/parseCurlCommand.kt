package com.app.krestbuilder.utils.curl

import com.app.krestbuilder.CurlComponents

private fun parseCurlCommand(curl: String): CurlComponents {
    val parts = curl.split(" ")
    var url = ""
    var method = "GET"
    val headers = mutableMapOf<String, String>()
    var body: String? = null

    var i = 0
    while (i < parts.size) {
        when (parts[i]) {
            "-X", "--request" -> {
                method = parts.getOrNull(i + 1) ?: "GET"
                i++
            }
            "-H", "--header" -> {
                val header = parts.getOrNull(i + 1)
                header?.split(":")?.let {
                    if (it.size == 2) {
                        headers[it[0].trim()] = it[1].trim()
                    }
                }
                i++
            }
            "--data", "--data-raw", "--data-binary", "--data-urlencode" -> {
                body = parts.getOrNull(i + 1) ?: ""
                i++
            }
            else -> if (url.isEmpty() && parts[i].startsWith("http")) {
                url = parts[i]
            }
        }
        i++
    }

    if (url.isEmpty()) {
        throw IllegalArgumentException("URL not found in cURL command.")
    }

    return CurlComponents(url, method, headers, body)
}
