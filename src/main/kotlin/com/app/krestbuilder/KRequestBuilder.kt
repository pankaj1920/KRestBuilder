package com.app.krestbuilder

import com.android.tools.idea.appinspection.inspectors.backgroundtask.view.capitalizedName
import com.app.krestbuilder.remote.curl.extractor.HttpMethodType
import com.app.krestbuilder.remote.ktorClient
import com.app.krestbuilder.ui.CurlInputDialog
import com.app.krestbuilder.ui.event.getDataContextVirtualFile
import com.app.krestbuilder.utils.Print
import com.app.krestbuilder.utils.filemanager.FunctionPrefix
import com.app.krestbuilder.utils.filemanager.createPackageFile
import com.app.krestbuilder.utils.filemanager.getFuncName
import com.app.krestbuilder.utils.generator.domain.model.uidata.ResponseUiDataGenerator
import com.app.krestbuilder.utils.generator.remote.dto.RequestBodyGenerator
import com.app.krestbuilder.utils.generator.remote.dto.ResponseDtoGenerator
import com.app.krestbuilder.utils.generator.remote.endpoint.EndPointGenerator
import com.app.krestbuilder.utils.generator.remote.service.ServiceGenerator
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
import io.ktor.http.*
import okhttp3.OkHttpClient
import okhttp3.RequestBody

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
        CurlInputDialog { className, funName, curlCommand ->
            Print.log("fileName from callback == $className")
            Print.log("curlCommand from callback == $curlCommand")

            if (className.isEmpty()) {
                Messages.showErrorDialog("Class Name cannot be empty.", "Validation Error")
                return@CurlInputDialog
            }
            if (funName.isEmpty()) {
                Messages.showErrorDialog("FunctionPrefix cannot be empty.", "Validation Error")
                return@CurlInputDialog
            }

            if (curlCommand.isEmpty()) {
                Messages.showErrorDialog("cURL Command cannot be empty.", "Validation Error")
                return@CurlInputDialog
            }

            try {
                val jsonResponse = ktorClient(curlCommand)
                val functionName = getFuncName(funName, jsonResponse.method)

                Print.log("functionName => $functionName")

                Messages.showMessageDialog(
                    project,
                    "URL: ${jsonResponse.url} \n\n" +
                            "Method: ${jsonResponse.method} \n\n" +
                            "Body:\n ${jsonResponse.body} \n\n" +
                            "Response:\n ${jsonResponse.response} \n\n",
                    "Success",
                    Messages.getInformationIcon()
                )

                val responseClassName = ResponseDtoGenerator().generateResponseDtoFile(
                    json = jsonResponse.response, psiDirectory = psiDirectory, clazzName = className, project = project
                )

                val requestClassName = RequestBodyGenerator().generateRequestBodyFile(
                    json = jsonResponse.body, psiDirectory = psiDirectory, clazzName = className, project = project

                )
                val responseUiDataClassName = ResponseUiDataGenerator().generateUiResponseDtoFile(
                    json = jsonResponse.response,
                    psiDirectory = psiDirectory,
                    clazzName = className,
                    project = project
                )

                val endPontName =
                    EndPointGenerator().generateEndPointFile(
                        clazzName = className,
                        url = jsonResponse.url.toString(),
                        psiDirectory = psiDirectory,
                        project = project
                    )
                ServiceGenerator().generateApiServiceFiles(
                    psiDirectory = psiDirectory,
                    className = className,
                    functionName = functionName,
                    requestBodyName = requestClassName,
                    responseName = responseClassName,
                    project = project,
                    urlName = endPontName
                )



                Messages.showInfoMessage("POJO classes generated successfully.", "Success")
            } catch (e: Exception) {
                Messages.showErrorDialog("Error: ${e.message}", "Failed to Generate POJO")
            }
        }.showAndGet()
    }


}
