package com.app.krestbuilder.utils.json

import com.google.gson.Gson
import com.google.gson.JsonElement

fun String.isJSONSchema(): Boolean {
    val jsonElement = Gson().fromJson(this, JsonElement::class.java)
    return if (jsonElement.isJsonObject) {
        with(jsonElement.asJsonObject) {
            has("\$schema")
        }
    } else {
        false
    }
}
