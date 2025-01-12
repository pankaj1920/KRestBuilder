package com.app.krestbuilder.utils.curl


enum class HttpMethod {
    GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD
}

data class CurlInfo(
    val url: String = "",
    val method: HttpMethod = HttpMethod.GET
)

fun getUrlAndMethod(curl: String): CurlInfo {
    val methodRegex = Regex("curl --location(?: --request (\\w+))?")
    val urlRegex = Regex("'(https?://[^']+)'")
    val headRegex = Regex("(?<!\\w)--head(?!\\w)")

    // Extract the HTTP method
    val methodMatch = methodRegex.find(curl)

    val methodName = when {
        headRegex.containsMatchIn(curl) -> "head"  // Explicitly check for `--head`
        else -> methodMatch?.groups?.get(1)?.value?.lowercase()
    }
    println("methodName ==> $methodName")
    val method = HttpMethod.entries.find { it.name.lowercase() == methodName } ?: HttpMethod.GET

    // Extract the URL
    val urlMatch = urlRegex.find(curl)
    val url = urlMatch?.groups?.get(1)?.value ?: "NoUrlFound"

    return CurlInfo(url = url, method = method)
}


/*fun getUrlAndMethod(curl: String): CurlInfo {
//    val methodRegex = Regex("curl --location(?: --request (\\w+))?")
    val methodRegex = Regex("curl --location(?: --request (\\w+))?")
    val urlRegex = Regex("'(https?://[^']+)'")

    // Extract the HTTP method
    val methodMatch = methodRegex.find(curl)
    *//* val methodName = methodMatch?.groups?.get(1)?.value?.lowercase()
     println("methodName ==> ${methodMatch?.groups?.get(1)?.value?.lowercase()}")
     val method = HttpMethod.entries.find { it.name.lowercase() == methodName } ?: HttpMethod.GET
 *//*
    val methodName = when {
        curl.contains("--head") -> "head" // Explicitly check for `--head`
        else -> methodMatch?.groups?.get(1)?.value?.lowercase()
    }
    println("methodName ==> $methodName")
    val method = HttpMethod.entries.find { it.name.lowercase() == methodName } ?: HttpMethod.GET

    // Extract the URL
    val urlMatch = urlRegex.find(curl)
    val url = urlMatch?.groups?.get(1)?.value ?: "NoUrlFound"

    return CurlInfo(url = url, method = method)
}*/


fun extractUrlAndMethod(curl: String): Pair<String, String>? {
    val methodMap = mapOf(
        "curl --location --request PUT" to "PUT",
        "curl --location --request PATCH" to "PATCH",
        "curl --location --request DELETE" to "DELETE",
        "curl --location --request OPTIONS" to "OPTIONS",
        "curl --location --head" to "HEAD",
        "curl --location" to "GETPOST" // Default to GET for simple `curl --location`
    )

    val lines = curl.lines()

    for ((key, method) in methodMap) {
        val line = lines.find { it.startsWith(key) }
        if (line != null) {
            val url = line.split(" '").getOrNull(1)?.split("'")?.getOrNull(0)
            if (url != null) {
                return Pair(url, method)
            }
        }
    }
    return null // Return null if no matching method or URL is found
}
