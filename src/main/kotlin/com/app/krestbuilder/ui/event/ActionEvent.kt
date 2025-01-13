package com.app.krestbuilder.ui.event

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile

fun AnActionEvent.getDataContextVirtualFile(): VirtualFile? {
    return this.dataContext.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
}
