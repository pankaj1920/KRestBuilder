package com.app.krestbuilder.utils.filemanager

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.idea.KotlinFileType


fun createPackageFile(
    pojoClasses: String,
    psiDirectory: PsiDirectory,
    className: String,
    project: Project,
    directoryPath: String? = null,
) {
    WriteCommandAction.runWriteCommandAction(project) {
        var currentDirectory: PsiDirectory = psiDirectory
        directoryPath?.let {
            val directorySegments = directoryPath.split("/")
            for (segment in directorySegments) {
                currentDirectory = currentDirectory.findSubdirectory(segment)
                    ?: currentDirectory.createSubdirectory(segment)
            }
        }

        val file = PsiFileFactory.getInstance(project).createFileFromText(
            "$className.kt",
            KotlinFileType.INSTANCE,
            pojoClasses
        )
        currentDirectory.add(file)
    }
}

fun generateClass(
    pojoClasses: String,
    psiDirectory: PsiDirectory,
    className: String,
    project: Project
) {
    WriteCommandAction.runWriteCommandAction(project) {
        // Create and add the file
        val file = PsiFileFactory.getInstance(project).createFileFromText(
            "$className.kt",
            KotlinFileType.INSTANCE,
            pojoClasses
        )
        psiDirectory.add(file)
    }
}
