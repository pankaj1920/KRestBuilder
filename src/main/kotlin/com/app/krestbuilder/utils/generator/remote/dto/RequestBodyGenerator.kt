package com.app.krestbuilder.utils.generator.remote.dto

import com.android.tools.r8.S
import com.app.krestbuilder.utils.Print
import com.app.krestbuilder.utils.filemanager.ClassFilePrefix.REQUES_FILE_PREFIXT
import com.app.krestbuilder.utils.filemanager.FileDirectory.REQUEST_BODY_PATH
import com.app.krestbuilder.utils.filemanager.FileDirectory.RESPONSE_DTO_PATH
import com.app.krestbuilder.utils.filemanager.createPackageFile
import com.app.krestbuilder.utils.generator.remote.dto.utils.requestResponseFromJson
import com.app.krestbuilder.utils.json.gson
import com.app.krestbuilder.utils.json.pojoFromJson
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class RequestBodyGenerator {
    fun generateRequestBodyFile(json: String?, psiDirectory: PsiDirectory, clazzName: String, project: Project) :String{
        val className = clazzName + REQUES_FILE_PREFIXT
        if (!json.isNullOrBlank() && json.trim() != "{}") {
            json.let {
                val pojoClasses = gson.requestResponseFromJson(json, className)
                createPackageFile(pojoClasses, psiDirectory, className, project, REQUEST_BODY_PATH)
            }

        }

        return clazzName

    }
}