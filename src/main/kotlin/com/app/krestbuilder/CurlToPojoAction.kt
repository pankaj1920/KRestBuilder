package com.app.krestbuilder

import com.app.krestbuilder.remote.ktorClient
import com.app.krestbuilder.ui.event.getDataContextVirtualFile
import com.app.krestbuilder.utils.filemanager.generateClass
import com.app.krestbuilder.utils.json.pojoFromJson
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

class CurlToPojoAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        // Get the selected directory (package)
        val project: Project = event.project ?: return
        val dataContext = event.dataContext
        val psiDirectory: PsiDirectory? =
            PsiManager.getInstance(project).findDirectory(event.getDataContextVirtualFile() ?: return)

        if (psiDirectory == null) {
            Messages.showErrorDialog("Could not find target package directory.", "Error")
            return
        }

        // Show input dialog to get cURL command
        val curlCommand = Messages.showInputDialog(
            project,
            "Enter the cURL command:",
            "Generate POJO from cURL",
            Messages.getQuestionIcon()
        ) ?: return

        try {
            val jsonResponse = ktorClient(curlCommand)
            // Generate POJO classes
            val pojoClasses = gson.pojoFromJson(jsonResponse.response.toString(),"")

            val project = event.project
            Messages.showMessageDialog(
                project,
                "Response:\n\n $jsonResponse \n\nPOJO classes generated successfully:\n\n$pojoClasses",
                "Success",
                Messages.getInformationIcon()
            )

            // Generate and add the POJO class to the selected package
            val className = "GeneratedPojo" // You can enhance this to get a user-provided name
            if (project != null) {
                generateClass(pojoClasses, psiDirectory, className, project)
            }

            Messages.showInfoMessage("POJO classes generated successfully.", "Success")
        } catch (e: Exception) {
            Messages.showErrorDialog("Error: ${e.message}", "Failed to Generate POJO")
        }
    }


}

// Helper extension function to get VirtualFile from event
