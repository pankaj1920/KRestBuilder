package com.app.krestbuilder.remote

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class ApiClient(
    private val httpClient: OkHttpClient
) {

    /*fun makeApiRequest(
        url: String,
        method: String,
        headers: Map<String, String>,
        body: String?
    ): String {
        val requestBuilder = Request.Builder().url(url)

        headers.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }

        val request = when (method.uppercase()) {
            "POST" -> requestBuilder.post(body?.toRequestBody() ?: "".toRequestBody()).build()
            "PUT" -> requestBuilder.put(body?.toRequestBody() ?: "".toRequestBody()).build()
            else -> requestBuilder.get().build()
        }

        val response: Response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("HTTP error: ${response.code}")
        return response.body?.string() ?: "Empty response body"
    }*/

    fun makeApiRequest(url: String, method: String, headers: Map<String, String>, body: String?): String {
        val requestBuilder = Request.Builder().url(url)

        // Add headers
        headers.forEach { (key, value) -> requestBuilder.addHeader(key, value) }

        // Set the method and body
        val requestBody = body?.toRequestBody("application/json".toMediaTypeOrNull())
        requestBuilder.method(method, requestBody)

        val request = requestBuilder.build()
        val response = httpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("HTTP error: ${response.code}")
        }

        return response.body?.string() ?: throw Exception("Response body is null.")
    }

}