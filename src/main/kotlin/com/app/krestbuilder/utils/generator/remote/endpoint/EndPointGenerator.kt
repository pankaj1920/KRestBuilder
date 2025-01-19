package com.app.krestbuilder.utils.generator.remote.endpoint

import com.app.krestbuilder.utils.filemanager.FileDirectory.REMOTE_PATH
import com.app.krestbuilder.utils.filemanager.FileDirectory.RESPONSE_DTO_PATH
import com.app.krestbuilder.utils.filemanager.createPackageFile
import com.app.krestbuilder.utils.generator.remote.endpoint.utils.getEndPointSkeleton
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

private val ENDPOINT = "Endpoints"

class EndPointGenerator {
    fun generateEndPointFile(clazzName: String, url: String, psiDirectory: PsiDirectory, project: Project): String {
        val endPointName = clazzName.uppercase()
        val endPointStruct = getEndPointSkeleton(endPointName = endPointName, url = url).trimIndent()
        createPackageFile(endPointStruct, psiDirectory, ENDPOINT, project, REMOTE_PATH)
        return endPointName
    }
}