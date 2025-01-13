package com.app.krestbuilder.remote

import com.app.krestbuilder.remote.curl.extractor.HttpMethod
import com.app.krestbuilder.remote.curl.extractor.getBody
import com.app.krestbuilder.remote.curl.extractor.getHeaderMap
import com.app.krestbuilder.remote.curl.extractor.getUrlAndMethod
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


/* fun ktorClient(curl: String): String {

    val client = HttpClient()
    val (url, method) = getUrlAndMethod(curl)
    val header = getHeaders(curl)
    val body = getBody(curl)




}*/
fun ktorClient(curl: String): String {
   // Create an HTTP client
   val client = HttpClient()

   // Extract URL, method, headers, and body
   val curlInfo = getUrlAndMethod(curl)
   val url = curlInfo.url
   val method = curlInfo.method
   val headers = getHeaderMap(curl)
   val body = getBody(curl)

   // Perform the HTTP request
   return runBlocking {
      try {
         val response = client.request {
            this.url(url)
            this.method = when (method) {
               HttpMethod.GET -> io.ktor.http.HttpMethod.Get
               HttpMethod.POST -> io.ktor.http.HttpMethod.Post
               HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
               HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
               HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
               HttpMethod.HEAD -> io.ktor.http.HttpMethod.Head
               HttpMethod.OPTIONS -> io.ktor.http.HttpMethod.Options
            }

            // Set headers
            headers.forEach { (key, value) ->
               header(key, value)
            }

            // Set body if applicable
            if (body.isNotBlank() && (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH)) {
               setBody(body)
            }
         }
         response.bodyAsText() // Return the response body as a string
      } catch (e: Exception) {
         "Error: ${e.message}"
      } finally {
         client.close()
      }
   }
}


