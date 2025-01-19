package com.app.krestbuilder.utils.filemanager


fun String.getModelClazzName(isResponse: Boolean): String {
    return when {
        !isResponse -> "${this}DomainModel"
        else -> "${this}Dto"
    }
}