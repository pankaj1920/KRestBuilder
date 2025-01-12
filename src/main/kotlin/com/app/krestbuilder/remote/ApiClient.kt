package com.app.krestbuilder.remote

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class ApiClient(
    private val httpClient: OkHttpClient
) {


    fun makeApiRequest(
        url: String,
        method: String,
        headers: Map<String, String>,
        body: String?
    ): String {


        val client = OkHttpClient()

        val builder = Request.Builder().url(url)

        // Add headers
        headers.forEach { (key, value) ->
            builder.addHeader(key, value)
        }

        // Set method and body
        val requestBody = body?.toRequestBody("application/json".toMediaType())
        builder.method(method,requestBody)

        val request = builder.build()

        client.newCall(request).execute().use { response ->

            println("URL: $url")
            println("Method: $method")
            println("Headers: $headers")
            println("Body: $body")
            println("Response Code: ${response.code}")
            println("Response Message: ${response.message}")
            println("Response Body: ${response.body?.string()}")

            if (!response.isSuccessful) {
                throw IOException("Unexpected code: ${response.code} - ${response.message}")
            }
            return response.body?.string() ?: throw IOException("Empty response body")
        }
    }


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

//    fun makeApiRequest(url: String, method: String, headers: Map<String, String>, body: String?): String {
//        val client = OkHttpClient()
//        val requestBuilder = Request.Builder().url(url)
//
//        // Add headers
//        headers.forEach { (key, value) ->
//            requestBuilder.addHeader(key, value)
//        }
//
//        // Set method and body
//        val request = when (method.uppercase()) {
//            "POST", "PUT", "PATCH" -> {
//                val requestBody = body?.toRequestBody("application/json".toMediaTypeOrNull())
//                    ?: "".toRequestBody()
//                requestBuilder.method(method.uppercase(), requestBody).build()
//            }
//            "GET", "DELETE" -> requestBuilder.method(method.uppercase(), null).build()
//            else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
//        }
//
//        // Execute request
//        client.newCall(request).execute().use { response ->
//            if (!response.isSuccessful) {
//                throw IOException("Unexpected code: ${response.code} - ${response.message}")
//            }
//            return response.body?.string() ?: throw IOException("Empty response body")
//        }
//    }

}