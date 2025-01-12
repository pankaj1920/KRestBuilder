package com.app.krestbuilder.utils

import com.google.gson.Gson
import com.google.gson.JsonObject


import com.google.gson.*
import java.util.*

fun Gson.generatePojoFromJson(jsonResponse: String, className: String = "GeneratedPojo"): String {
    val jsonObject = this.fromJson(jsonResponse, JsonObject::class.java)
    val builder = StringBuilder()

    val nestedClasses = mutableListOf<String>()

    fun processObject(obj: JsonObject, currentClassName: String): String {
        val classBuilder = StringBuilder()
        classBuilder.append("@Serializable\ndata class $currentClassName(\n")

        obj.keySet().forEach { key ->
            val value = obj[key]
            val type = when {
                value.isJsonObject -> {
                    val nestedClassName = key.capitalize(Locale.ROOT)
                    nestedClasses.add(processObject(value.asJsonObject, nestedClassName))
                    nestedClassName
                }

                value.isJsonArray -> {
                    val elementType = if (value.asJsonArray.size() > 0) {
                        // Check the type of the first element in the array
                        when {
                            value.asJsonArray[0].isJsonObject -> {
                                // If the first element is a JSON object, process it recursively
                                val nestedClassName = key.capitalize(Locale.ROOT)
                                nestedClasses.add(processObject(value.asJsonArray[0].asJsonObject, nestedClassName))
                                "List<$nestedClassName>"
                            }

                            value.asJsonArray[0].isJsonPrimitive && value.asJsonArray[0].asJsonPrimitive.isString -> {
                                // If the first element is a string, treat it as a list of strings
                                "List<String>"
                            }

                            value.asJsonArray[0].isJsonPrimitive && value.asJsonArray[0].asJsonPrimitive.isNumber -> {
                                // If the first element is a number, treat it as a list of integers
                                "List<Int>"
                            }

                            else -> {
                                // Default to List<Any> if it's not a string, number, or object
                                "List<Any>"
                            }
                        }
                    } else {
                        // Default case if the array is empty
                        "List<Any>"
                    }
                    elementType
                }

                value.isJsonPrimitive -> {
                    val primitive = value.asJsonPrimitive
                    when {
                        primitive.isBoolean -> "Boolean"
                        primitive.isNumber -> if (primitive.toString().contains(".")) "Double" else "Int"
                        primitive.isString -> "String"
                        else -> "String" // Fallback for unknown primitive types
                    }
                }

                value.isJsonNull -> "Any?" // Kotlin nullable type for nulls
                else -> "Any" // Fallback for unexpected cases
            }

            classBuilder.append("\t@SerialName(\"$key\")\n\tval $key: $type,\n")
        }

        // Remove the trailing comma and close the class definition
        if (classBuilder.endsWith(",\n\n")) {
            classBuilder.delete(classBuilder.length - 2, classBuilder.length)
        }
        classBuilder.append(")\n\n")
        return classBuilder.toString()
    }

    builder.append(
        "import kotlinx.serialization.Serializable \n" + "import kotlinx.serialization.SerialName\n\n"
    )
    builder.append(processObject(jsonObject, className))
    nestedClasses.reversed().forEach { builder.append(it) }

    return builder.toString()
}


fun Gson.generatePojoFromJsonSingle(jsonResponse: String): String {
    val jsonObject = this.fromJson(jsonResponse, JsonObject::class.java)
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
