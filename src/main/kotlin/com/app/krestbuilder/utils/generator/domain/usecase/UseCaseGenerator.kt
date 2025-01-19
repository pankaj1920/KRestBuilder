package com.app.krestbuilder.utils.generator.domain.usecase

import com.app.krestbuilder.utils.filemanager.FileDirectory.RESPONSE_DTO_PATH
import com.app.krestbuilder.utils.filemanager.createPackageFile
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class UseCaseGenerator {
    fun generateUseCaseFile(pojoClasses: String, psiDirectory: PsiDirectory, className: String, project: Project) {
        createPackageFile(pojoClasses, psiDirectory, className, project, RESPONSE_DTO_PATH)
    }
}