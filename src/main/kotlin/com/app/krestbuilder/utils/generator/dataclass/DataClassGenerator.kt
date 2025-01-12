package com.app.krestbuilder.utils.generator.dataclass

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.idea.KotlinFileType

 fun generateDataClass(
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
