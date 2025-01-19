package com.app.krestbuilder.utils.generator.remote.service.utils

/*

fun getServiceSkeleton(
    className: String, functionName: String, requestBodyName: String, responseBodyName: String
): String {
    val request = if (requestBodyName.isNotBlank()) "request: $requestBodyName" else ""
    return """
        interface ${className}ApiService {
                suspend fun $functionName($request):${getSelectionReturnType(responseBodyName)}
            }
        """
}

fun getSelectionReturnType(responseBodyName: String) = "Flow<NetworkState<BaseResponse<$responseBodyName>>>"

*/

fun getServiceSkeleton(
    cName: String, fName: String, rqBodyName: String, rpBodyName: String
): String {
    val request = if (rqBodyName.isNotBlank()) "request: $rqBodyName" else ""
    val placeholders = mapOf(
        "{{CLASS_NAME}}" to "${cName}ApiService",
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


