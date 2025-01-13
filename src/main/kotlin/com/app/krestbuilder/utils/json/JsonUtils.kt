package com.app.krestbuilder.utils.json

import com.app.krestbuilder.utils.Print
import com.google.gson.Gson
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

val gson =Gson()
val JSON = Json {
    isLenient = true
    /** if a field is defined as nullable in DTO and not present in JSON object from backend then that field will be skipped **/
    explicitNulls = false
    prettyPrint = true
    ignoreUnknownKeys = true
}


inline fun <reified T> decodeFromJsonString(json: String): T? {
    return try {
        JSON.decodeFromString(json)
    } catch (e: SerializationException) {
        e.printStackTrace()
        null
    }
}

inline fun <reified T> T.encodeToJsonString(): String? {
    return try {
        JSON.encodeToString(serializer(), this)
    } catch (e: SerializationException) {
        e.printStackTrace()
        null
    }
}

fun <T : Any> fromJsonToMap(json: String): Map<String, kotlinx.serialization.json.JsonElement> {
    return try {
        JSON.decodeFromString<Map<String, kotlinx.serialization.json.JsonElement>>(string = json)
    } catch (e: SerializationException) {
        Print.log("Parse Error (fromJsonToMap): ${e.message}")
        emptyMap()
    }
}


fun Map<String, String>.mapToJson(): String {
    val gson = Gson()
    return gson.toJson(this)
}