package com.app.krestbuilder

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


class CurlToPojoAction1 : AnAction() {
    private val httpClient = OkHttpClient()
    private val gson = Gson()

    override fun actionPerformed(event: AnActionEvent) {
        // Show input dialog to get cURL command
        val curlCommand = Messages.showInputDialog(
            event.project,
            "Enter the cURL command:",
            "Generate POJO from cURL",
            Messages.getQuestionIcon()
        ) ?: return

        try {
            // Parse the cURL command
            val (url, method, headers, body) = parseCurlCommand(curlCommand)

            // Make the API request
            val jsonResponse = makeApiRequest(url, method, headers, body)

            // Generate POJO classes
            val pojoClasses = generatePojoFromJson(jsonResponse)

            // Output the POJO classes
            val project = event.project
            Messages.showMessageDialog(
                project,
                "POJO classes generated successfully:\n\n$pojoClasses",
                "Success",
                Messages.getInformationIcon()
            )

        } catch (e: Exception) {
            Messages.showErrorDialog("Error: ${e.message}", "Failed to Generate POJO")
        }
    }

    // Parse the cURL command into components
    private fun parseCurlCommand(curl: String): CurlComponents {
        // Dummy implementation. Parse the cURL properly using regex or libraries.
        val url = "https://jsonplaceholder.typicode.com/posts/1" // Extract this from cURL
        val method = "GET" // Extract method
        val headers = mapOf("Authorization" to "Bearer token") // Extract headers
        val body: String? = null // Extract body if POST/PUT

        return CurlComponents(url, method, headers, body)
    }

    // Make the API request
    private fun makeApiRequest(
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
        return response.body?.string() ?: throw Exception("Empty response body")
    }

    // Generate POJO classes from JSON
    private fun generatePojoFromJson(jsonResponse: String): String {
        val jsonObject = gson.fromJson(jsonResponse, JsonObject::class.java)
        val builder = StringBuilder()

        jsonObject.keySet().forEach { key ->
            val value = jsonObject[key]

            val type = when {
                value.isJsonObject -> "Object"
                value.isJsonArray -> "List<Object>"
                value.isJsonPrimitive -> {
                    val primitive = value.asJsonPrimitive
                    when {
                        primitive.isBoolean -> "Boolean"
                        primitive.isNumber -> "Double" // Use Double for generic numeric types
                        primitive.isString -> "String"
                        else -> "String" // Fallback for unknown primitive types
                    }
                }
                value.isJsonNull -> "Any?" // Kotlin nullable type for nulls
                else -> "Any" // Fallback for unexpected cases
            }

            builder.append("var $key: $type\n")
        }

        return builder.toString()
    }
}

// Data class to represent parsed cURL components
data class CurlComponents1(
    val url: String,
    val method: String,
    val headers: Map<String, String>,
    val body: String?
)
