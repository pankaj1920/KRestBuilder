package com.app.krestbuilder.utils

import com.app.krestbuilder.domain.classgenerator.JsonObjectDataClassGenerator
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject


class KotlinClassGenerator(private val rootClassName: String, private val json: String) {

    fun makeKotlinClass() {
        when {
//            json.isJSONObject() -> JsonObjectDataClassGenerator(
//                rootClassName,
//                Gson().fromJson(json, JsonObject::class.java)
//            ).generate(isTop = true)
//                json.isJSONArray() -> ListClassGeneratorByJSONArray(rootClassName, json).generate()
            else -> throw IllegalStateException("Can't generate Kotlin Data Class from a no JSON Object/JSON Object Array")
        }

    }

    private fun String.isJSONObject(): Boolean {
        val jsonElement = Gson().fromJson(this, JsonElement::class.java)
        return jsonElement.isJsonObject
    }

    private fun String.isJSONArray(): Boolean {
        val jsonElement = Gson().fromJson(this, JsonElement::class.java)
        return jsonElement.isJsonArray
    }
}