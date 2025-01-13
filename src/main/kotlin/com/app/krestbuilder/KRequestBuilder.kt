package com.app.krestbuilder

import com.app.krestbuilder.remote.ktorClient
import com.app.krestbuilder.ui.CurlInputDialog
import com.app.krestbuilder.ui.event.getDataContextVirtualFile
import com.app.krestbuilder.utils.Print
import com.app.krestbuilder.utils.json.pojoFromJson
import com.app.krestbuilder.utils.generator.dataclass.generateDataClass
import com.app.krestbuilder.utils.json.JSON
import com.app.krestbuilder.utils.json.gson
import com.google.gson.Gson
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import okhttp3.OkHttpClient

class KRequestBuilder : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project: Project = event.project ?: return
        val psiDirectory: PsiDirectory? =
            PsiManager.getInstance(project).findDirectory(event.getDataContextVirtualFile() ?: return)

        if (psiDirectory == null) {
            Messages.showErrorDialog("Could not find target package directory.", "Error")
            return
        }

        // Show the dialog and handle user inputs
        CurlInputDialog { className, curlCommand ->
            Print.log("fileName from callback == $className")
            Print.log("curlCommand from callback == $curlCommand")

            if (className.isEmpty()) {
                Messages.showErrorDialog("Class Name cannot be empty.", "Validation Error")
                return@CurlInputDialog
            }

            if (curlCommand.isEmpty()) {
                Messages.showErrorDialog("cURL Command cannot be empty.", "Validation Error")
                return@CurlInputDialog
            }

            try {
                val jsonResponse = ktorClient(curlCommand)
                val pojoClasses = gson.pojoFromJson(jsonResponse,className+"ResponseDto")

                Messages.showMessageDialog(
                    project,
                    "Response:\n\n $jsonResponse \n\nPOJO classes generated successfully:\n\n$pojoClasses",
                    "Success",
                    Messages.getInformationIcon()
                )

                generateDataClass(pojoClasses, psiDirectory, className, project)
                Messages.showInfoMessage("POJO classes generated successfully.", "Success")
            } catch (e: Exception) {
                Messages.showErrorDialog("Error: ${e.message}", "Failed to Generate POJO")
            }
        }.showAndGet()
    }
}
