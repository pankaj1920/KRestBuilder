package com.app.krestbuilder

import com.app.krestbuilder.mock.sampleListData
import com.app.krestbuilder.remote.ApiClient
import com.app.krestbuilder.utils.generatePojoFromJson
import com.app.krestbuilder.utils.generator.dataclass.generateDataClass
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
    private val httpClient = OkHttpClient()
    private val gson = Gson()

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
            // Parse the cURL command
            val (url, method, headers, body) = parseCurlCommand(curlCommand)

            // Make the API request
            val jsonResponse = ApiClient(httpClient).makeApiRequest(url, method, headers, body)

            // Generate POJO classes
            val pojoClasses = gson.generatePojoFromJson(jsonResponse)

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
                generateDataClass(pojoClasses, psiDirectory, className, project)
            }

            Messages.showInfoMessage("POJO classes generated successfully.", "Success")
        } catch (e: Exception) {
            Messages.showErrorDialog("Error: ${e.message}", "Failed to Generate POJO")
        }
    }

    private fun parseCurlCommand(curl: String): CurlComponents {
        // Dummy implementation. Parse the cURL properly using regex or libraries.
        val url = curl // Extract this from cURL
        val method = "GET" // Extract method
        val headers = mapOf("Authorization" to "Bearer token") // Extract headers
        val body: String? = null // Extract body if POST/PUT

        return CurlComponents(url, method, headers, body)
    }

}

// Helper extension function to get VirtualFile from event
private fun AnActionEvent.getDataContextVirtualFile(): VirtualFile? {
    return this.dataContext.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
}

data class CurlComponents(
    val url: String,
    val method: String,
    val headers: Map<String, String>,
    val body: String?
)
