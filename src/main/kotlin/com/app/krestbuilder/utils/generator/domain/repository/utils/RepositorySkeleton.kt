package com.app.krestbuilder.utils.generator.domain.repository.utils



fun getRepositorySkeleton(
    cName: String, fName: String, rqBodyName: String, rpBodyName: String
): String {
    val request = if (rqBodyName.isNotBlank()) "request: $rqBodyName" else ""
    val placeholders = mapOf(
        "{{CLASS_NAME}}" to "${cName}Repository",
        "{{FUN_NAME}}" to fName,
        "{{REQUEST_BODY}}" to request,
        "{{RESPONSE_BODY}}" to rpBodyName
    )
    val serviceSkt = """
      interface class {{CLASS_NAME}} {
            interface suspend fun {{FUN_NAME}}({{REQUEST_BODY}}): MyResult<ApiResponse<{{RESPONSE_BODY}}>, CompleteApiError<Error>>
        }
    """

    return placeholders.entries.fold(serviceSkt) { acc, entry ->
        acc.replace(entry.key, entry.value)
    }
}


