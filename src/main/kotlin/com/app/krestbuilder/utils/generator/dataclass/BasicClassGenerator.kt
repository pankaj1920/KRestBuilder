package com.app.krestbuilder.utils.generator.dataclass

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.idea.KotlinFileType

fun generateBasicClass(
    pojoClasses: String,
    psiDirectory: PsiDirectory,
    className: String,
    project: Project
) {
    WriteCommandAction.runWriteCommandAction(project) {
        try {
            // Check if a file with the same name already exists
            val existingFile = psiDirectory.findFile("$className.kt")
            if (existingFile != null) {
                Messages.showErrorDialog("File $className.kt already exists in the selected package.", "File Conflict")
                return@runWriteCommandAction
            }

            // Create a new Kotlin file
            val file = PsiFileFactory.getInstance(project).createFileFromText(
                "$className.kt",
                KotlinFileType.INSTANCE,
                "class $className {\n$pojoClasses\n}"
            )

            // Add the file to the directory
            psiDirectory.add(file)

            // Reformat the file to adhere to Kotlin code style
            CodeStyleManager.getInstance(project).reformat(file)
        } catch (e: Exception) {
            Messages.showErrorDialog("Failed to create the file: ${e.message}", "Error")
        }
    }
}
