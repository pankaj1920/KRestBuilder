package com.app.krestbuilder.domain.classgenerator

import com.google.gson.JsonObject
import org.gradle.internal.impldep.com.google.gson.annotations.SerializedName

class JsonObjectDataClassGenerator(
    @SerializedName("")
    private val className: String,
    private val jsonObject: JsonObject
) {

}


