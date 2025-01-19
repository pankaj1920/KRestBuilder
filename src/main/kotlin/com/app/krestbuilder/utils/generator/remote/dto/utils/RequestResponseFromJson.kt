package com.app.krestbuilder.utils.generator.remote.dto.utils

import com.android.tools.idea.appinspection.inspectors.backgroundtask.view.capitalizedName
import com.app.krestbuilder.utils.filemanager.getModelClazzName
import com.google.gson.Gson
import com.google.gson.JsonObject


import java.util.*

fun Gson.requestResponseFromJson(jsonResponse: String, className: String, isResponse: Boolean = true): String {
    val jsonObject = this.fromJson(jsonResponse, JsonObject::class.java)
    val builder = StringBuilder()

    val nestedClasses = mutableListOf<String>()

    fun processObject(obj: JsonObject, currentClassName: String): String {
        val classBuilder = StringBuilder()
        classBuilder.append("@Serializable\ndata class ${currentClassName.getModelClazzName(isResponse)}(\n")

        obj.keySet().forEach { key ->
            val value = obj[key]
            val type = when {
                value.isJsonObject -> {
                    val nestedClassName = key.capitalize(Locale.ROOT)
                    val clazzName = nestedClassName.getModelClazzName(isResponse)
                    if (!nestedClasses.contains(clazzName)) {
                        nestedClasses.add(processObject(value.asJsonObject, nestedClassName))
                    }
                    clazzName
                }

                value.isJsonArray -> {
                    val elementType = if (value.asJsonArray.size() > 0) {
                        // Check the type of the first element in the array
                        when {
                            value.asJsonArray[0].isJsonObject -> {
                                // If the first element is a JSON object, process it recursively
                                val nestedClassName = key.capitalize(Locale.ROOT)
                                val clazzName = nestedClassName.getModelClazzName(isResponse)
                                if (!nestedClasses.contains(clazzName)) {
                                    nestedClasses.add(
                                        processObject(value.asJsonArray[0].asJsonObject, nestedClassName)
                                    )
                                }
                                "List<$clazzName>"
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

            // Append property with default value (null or empty list)
            val defaultValue = when {
                type.startsWith("List") -> "emptyList()"
                else -> "null"
            }
            if (isResponse) classBuilder.append("\t@SerialName(\"$key\")\n\tval $key:$type? = $defaultValue,\n")
            else classBuilder.append("\tval $key:$type? = $defaultValue,\n")

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


