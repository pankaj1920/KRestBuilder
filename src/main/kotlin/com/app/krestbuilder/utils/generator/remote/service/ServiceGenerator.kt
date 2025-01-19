package com.app.krestbuilder.utils.generator.remote.service

import com.app.krestbuilder.utils.filemanager.FileDirectory.REMOTE_PATH
import com.app.krestbuilder.utils.filemanager.FileDirectory.RESPONSE_DTO_PATH
import com.app.krestbuilder.utils.filemanager.createPackageFile
import com.app.krestbuilder.utils.generator.remote.service.utils.getServiceImplSkeleton
import com.app.krestbuilder.utils.generator.remote.service.utils.getServiceSkeleton
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class ServiceGenerator {
    fun generateServiceFile(pojoClasses: String, psiDirectory: PsiDirectory, className: String, project: Project) {
        createPackageFile(pojoClasses, psiDirectory, className, project, RESPONSE_DTO_PATH)
    }

    fun generateApiServiceFiles(
        psiDirectory: PsiDirectory,
        project: Project,
        className: String,
        functionName: String,
        requestBodyName: String,
        responseName: String,
        urlName: String
    ) {
        val apiServiceContent = getServiceSkeleton(
            cName = className,
            fName = functionName,
            rqBodyName = requestBodyName,
            rpBodyName = responseName
        ).trimIndent()

        val apiServiceImplContent = getServiceImplSkeleton(
            className = className,
            functionName = functionName,
            requestBodyName = requestBodyName,
            responseBodyName = responseName,
            urlName = urlName
        ).trimIndent()


        // Generate the files
        createPackageFile(
            pojoClasses = apiServiceContent,
            psiDirectory = psiDirectory,
            className = "${className}ApiService",
            project = project,
            directoryPath = REMOTE_PATH
        )

        createPackageFile(
            pojoClasses = apiServiceImplContent,
            psiDirectory = psiDirectory,
            className = "${className}ApiServiceImpl",
            project = project,
            directoryPath = REMOTE_PATH
        )
    }
}