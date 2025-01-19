package com.app.krestbuilder.utils.generator.remote.endpoint.utils


fun getEndPointSkeleton(
    endPointName: String, url: String
): String {

    return """
        object Endpoints {
               val $endPointName = ${url}
            }
        """
}
