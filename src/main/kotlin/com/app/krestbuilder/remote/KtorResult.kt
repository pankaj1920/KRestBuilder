package com.app.krestbuilder.remote

import com.app.krestbuilder.remote.curl.extractor.HttpMethodType

data class KtorResult(
    val method: HttpMethodType? = null,
    val url: String? = null,
    val body: String? = null,
    val header: String? = null,
    var response: String? = null
)
