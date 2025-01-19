package com.app.krestbuilder.utils.generator.domain.model.uidata

import com.app.krestbuilder.utils.filemanager.ClassFilePrefix.REQUES_FILE_PREFIXT
import com.app.krestbuilder.utils.filemanager.ClassFilePrefix.RESPONSE_FILE_PREFIX
import com.app.krestbuilder.utils.filemanager.FileDirectory.MODEL_PATH
import com.app.krestbuilder.utils.filemanager.FileDirectory.RESPONSE_DTO_PATH
import com.app.krestbuilder.utils.filemanager.createPackageFile
import com.app.krestbuilder.utils.generator.remote.dto.utils.requestResponseFromJson
import com.app.krestbuilder.utils.json.gson
import com.app.krestbuilder.utils.json.pojoFromJson
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class ResponseUiDataGenerator {
    fun generateUiResponseDtoFile(
        json: String?,
        psiDirectory: PsiDirectory,
        clazzName: String,
        project: Project
    ): String {
        val className = clazzName + RESPONSE_FILE_PREFIX
        json?.let {
            val pojoClasses =
                gson.requestResponseFromJson(jsonResponse = json, className = className, isResponse = false)
            createPackageFile(pojoClasses, psiDirectory, className, project, MODEL_PATH)
        }

        return className
    }
}