package com.app.krestbuilder.ui

import com.app.krestbuilder.utils.Print
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.dsl.builder.*
import com.sun.java.accessibility.util.AWTEventMonitor.addTextListener
import org.jetbrains.annotations.ApiStatus
import javax.swing.JComponent
import javax.swing.event.DocumentEvent
import kotlin.properties.Delegates

/*

class CurlInputDialog(
    private val onSubmit: (fileName: String, curlCommand: String) -> Unit
) : DialogWrapper(true) {

    private val curlModule = Model()

    init {
        title = "KRequest Builder"
        init() // Initializes the dialog
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row("Class Name:") {
                textField()
                    .columns(20)
                    .bindText(curlModule::fileName)
                    .comment("Enter the name for class")
            }
            row("cURL Command:") {
                textArea()
                    .rows(10)
                    .columns(40)
                    .bindText(curlModule::curlCommand)
                    .comment("Paste the cURL")
            }

            row {
                button("Sample Cancel", actionListener = {

                })

                button("Sample Ok", actionListener = {
                    Print.log("fileName == ${curlModule.fileName}")
                    Print.log("curlCommand == ${curlModule.curlCommand}")
                })
            }
        }
    }

    override fun doOKAction() {
        // Call the callback with the user inputs
        Print.log("fileName == ${curlModule.fileName}")
        Print.log("curlCommand == ${curlModule.curlCommand}")
        onSubmit(curlModule.fileName, curlModule.curlCommand)
        super.doOKAction() // Closes the dialog
    }
}

@ApiStatus.Internal
internal data class Model(
    var fileName: String = "None",
    var curlCommand: String = "none",
    )
*/


class CurlInputDialog(
    private val onSubmit: (fileName: String, curlCommand: String) -> Unit
) : DialogWrapper(true) {

    private val curlModule = Model()

    init {
        title = "KRequest Builder"
        init() // Initializes the dialog
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row("Request Name:") {
                val classNameField = textField()
                    .columns(20)
                    .applyToComponent {
                        text = curlModule.fileName
                        document.addDocumentListener(object : DocumentAdapter() {
                            override fun textChanged(e: DocumentEvent) {
                                curlModule.fileName = this@applyToComponent.text
                            }
                        })
                    }
                    .comment("Enter the name for class")
            }
            row("cURL Command:") {
                val curlCommandField = textArea()
                    .rows(10)
                    .columns(40)
                    .applyToComponent {
                        text = curlModule.curlCommand
                        document.addDocumentListener(object : DocumentAdapter() {
                            override fun textChanged(e: DocumentEvent) {
                                curlModule.curlCommand = this@applyToComponent.text
                            }
                        })
                    }
                    .comment("Paste the cUrl")
            }

        }
    }

    override fun doOKAction() {

        onSubmit(curlModule.fileName, curlModule.curlCommand)
        super.doOKAction() // Closes the dialog
    }
}

@ApiStatus.Internal
internal data class Model(
    var fileName: String = "",
    var curlCommand: String = "",
)
