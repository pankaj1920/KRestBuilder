package com.app.krestbuilder.utils.generator.domain.repository.utils


fun getRepositoryImplSkeleton(
    className: String, functionName: String, requestBodyName: String, responseBodyName: String, urlName: String
): String {
    val request = if (requestBodyName.isNotBlank()) "request: $requestBodyName" else ""
    return """
            class ${className}ApiServiceImpl(
                private val ktorClient: NetworkClient
            ) : ${className}ApiService {
                override suspend fun $functionName($request): ${getSelectionImplReturnType(responseBodyName)}{
                   ${getSelectionImplReturnSkeleton(responseBodyName = responseBodyName, urlName = urlName)}
                }
            }
        """.trimIndent()
}

fun getSelectionImplReturnType(responseBodyName: String) = "Flow<NetworkState<BaseResponse<$responseBodyName>>>"
fun getSelectionImplReturnSkeleton(responseBodyName: String, urlName: String) =
    "return ktorClient.ktorPostFlowRequest<$responseBodyName>(body = request, $urlName)"

