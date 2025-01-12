package com.app.krestbuilder.utils.curl

import com.app.krestbuilder.CurlComponents
import net.minidev.json.JSONObject

fun parseCurlCommand(curl: String): CurlComponents {
    val parts = curl.split("\\s+".toRegex()) // Split by whitespace
    var url = ""
    var method = ""
    val headers = mutableMapOf<String, String>()
    var body: String? = null

    var i = 0
    while (i < parts.size) {
        when (parts[i]) {
            "--location" -> {
                url = parts.getOrNull(i + 1)?.trim('\'', '"') // Remove surrounding quotes
                    ?: throw IllegalArgumentException("Missing URL after --location")
                i++
            }

            "-X", "--request" -> {
                method = parts.getOrNull(i + 1)?.trim('\'', '"') // Remove surrounding quotes
                    ?: throw IllegalArgumentException("Missing HTTP method after -X/--request")
                i++
            }

            "-H", "--header" -> {
                val header = parts.getOrNull(i + 1) ?: throw IllegalArgumentException("Missing header value after -H/--header")
                val colonIndex = header.indexOf(":")
                if (colonIndex > 0) {
                    val key = header.substring(0, colonIndex).trim()
                    val value = header.substring(colonIndex + 1).trim()
                    headers[key] = value
                } else {
                    throw IllegalArgumentException("Invalid header format: $header")
                }
                i++
            }

            "--data", "--data-raw", "--data-binary", "--data-urlencode" -> {
                body = parts.getOrNull(i + 1)?.trim('\'', '"') // Remove surrounding quotes
                    ?: throw IllegalArgumentException("Missing data after --data/--data-raw")
                i++
            }
        }
        i++
    }

    if (url.isEmpty()) {
        throw IllegalArgumentException("URL not found in cURL command.")
    }

    println("URL: $url")
    println("Method: $method")
    println("Headers: $headers")
    println("Body: $body")
    return CurlComponents(url, method, headers, body)
}


fun convertCurlToJson(curl: String): String {
    val lines = curl.lines()

//    val url = lines.find { it.startsWith("curl --location") }?.split(" '")?.get(1)?.trimEnd('\'')
//    val url = lines.find { it.startsWith("curl --location --request PUT") }?.split(" '")?.get(1)?.trimEnd('\'')
//    val url = lines.find { it.startsWith("curl --location --request PATCH") }?.split(" '")?.get(1)?.trimEnd('\'')
//    val url = lines.find { it.startsWith("curl --location --request DELETE") }?.split(" '")?.get(1)?.trimEnd('\'')
//    val url = lines.find { it.startsWith("curl --location --request OPTIONS") }?.split(" '")?.get(1)?.trimEnd('\'')
//    val url = lines.find { it.startsWith("curl --location --head") }?.split(" '")?.get(1)?.trimEnd('\'')



 /*   val url = lines.find { it.startsWith("curl --location") }?.split(" '")?.get(1)?.trimEnd('\'')
    val method = "GET" // Assuming GET as `--location` typically implies a GET request
    val headers = mutableMapOf<String, String>()

    lines.forEach { line ->
        if (line.startsWith("--header")) {
            val header = line.split("'")[1]
            val keyValue = header.split(": ", limit = 2)
            if (keyValue.size == 2) {
                headers[keyValue[0]] = keyValue[1]
            }
        }
    }

    val jsonObject = JSONObject().apply {
        put("url", url)
        put("method", method)
        put("headers", JSONObject(headers))
    }
*/
    return " "// Pretty-print JSON with indentation
}
