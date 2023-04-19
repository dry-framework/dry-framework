package dev.dry.http.response

import dev.dry.http.RequestId
import dev.dry.common.concurrent.TraceId
import java.time.LocalDateTime

abstract class ApiResponse(
    //@Schema(
        //required = true,
        //description = "status indicating whether the API request completed or halted with an error",
    //)
    val status: ApiResponseStatus,
    //@Schema(
        //required = true,
        //description = "unique server generated request tracing identifier",
    //)
    val traceId: TraceId,
    //
    val timestamp: LocalDateTime,
    //@Schema(
        //required = false,
        //description = "client specified request identifier",
    //)
    val requestId: RequestId? = null,
) {
    /*
    constructor(status: ApiResponseStatus, requestScope: RequestScope): this(
        status = status,
        traceId = requestScope.traceId.value,
        requestId = requestScope.requestId,
    )*/
}
