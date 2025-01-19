package com.app.krestbuilder.utils.filemanager

import com.android.tools.idea.appinspection.inspectors.backgroundtask.view.capitalizedName
import com.app.krestbuilder.remote.curl.extractor.HttpMethodType
import com.app.krestbuilder.utils.Print
import com.app.krestbuilder.utils.filemanager.FunctionPrefix.DELETE_FUN
import com.app.krestbuilder.utils.filemanager.FunctionPrefix.GET_FUN
import com.app.krestbuilder.utils.filemanager.FunctionPrefix.HEAD_FUN
import com.app.krestbuilder.utils.filemanager.FunctionPrefix.OPTIONS_FUN
import com.app.krestbuilder.utils.filemanager.FunctionPrefix.PATCH_FUN
import com.app.krestbuilder.utils.filemanager.FunctionPrefix.POST_FUN
import com.app.krestbuilder.utils.filemanager.FunctionPrefix.PUT_FUN

fun getFuncName(funName: String, method: HttpMethodType?): String {
    return when (method) {
        HttpMethodType.GET -> "${GET_FUN}$funName"
        HttpMethodType.POST -> "${POST_FUN}$funName"
        HttpMethodType.PUT -> "${PUT_FUN}$funName"
        HttpMethodType.PATCH -> "${PATCH_FUN}$funName"
        HttpMethodType.DELETE -> "${DELETE_FUN}$funName"
        HttpMethodType.OPTIONS -> "${OPTIONS_FUN}$funName"
        HttpMethodType.HEAD -> "${HEAD_FUN}$funName"
        null -> "${GET_FUN}$funName"
    }

}