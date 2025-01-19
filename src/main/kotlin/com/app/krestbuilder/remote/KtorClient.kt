package com.app.krestbuilder.remote

import com.app.krestbuilder.remote.curl.extractor.HttpMethodType
import com.app.krestbuilder.remote.curl.extractor.getBody
import com.app.krestbuilder.remote.curl.extractor.getHeaderMap
import com.app.krestbuilder.remote.curl.extractor.getUrlAndMethod
import com.app.krestbuilder.utils.json.mapToJson
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


fun ktorClient(curl: String): KtorResult {
    // Create an HTTP client
    val client = HttpClient()

    // Extract URL, method, headers, and body
    val curlInfo = getUrlAndMethod(curl)
    val url = curlInfo.url
    val method = curlInfo.method
    val headers = getHeaderMap(curl)
    val body = getBody(curl)
    val ktorResult = KtorResult(method = method, header = headers.mapToJson(), body = body, url = url)

    // Perform the HTTP request
    runBlocking {
        try {
            val response = client.request {
                this.url(url)
                this.method = when (method) {
                    HttpMethodType.GET -> io.ktor.http.HttpMethod.Get
                    HttpMethodType.POST -> io.ktor.http.HttpMethod.Post
                    HttpMethodType.PUT -> io.ktor.http.HttpMethod.Put
                    HttpMethodType.PATCH -> io.ktor.http.HttpMethod.Patch
                    HttpMethodType.DELETE -> io.ktor.http.HttpMethod.Delete
                    HttpMethodType.HEAD -> io.ktor.http.HttpMethod.Head
                    HttpMethodType.OPTIONS -> io.ktor.http.HttpMethod.Options
                }

                // Set headers
                headers.forEach { (key, value) ->
                    header(key, value)
                }

                // Set body if applicable
                if (body.isNotBlank() && (method == HttpMethodType.POST || method == HttpMethodType.PUT || method == HttpMethodType.PATCH)) {
                    setBody(body)
                }
            }
            // Return the response body as a string
            ktorResult.response = response.bodyAsText()

        } catch (e: Exception) {
            "Error: ${e.message}"
        } finally {
            client.close()
        }

    }
    return ktorResult
}


