package dev.dry.http.response

import dev.dry.http.RequestId
import dev.dry.common.concurrent.TraceId
import dev.dry.http.response.ApiResponseStatus.ERROR
import java.time.LocalDateTime

class ApiErrorResponse(
    /*
    @Schema(
        required = true,
        description = "Request processing error",
    )*/
    val error: ApiError,
    timestamp: LocalDateTime,
    traceId: TraceId,
    requestId: RequestId? = null,
): ApiResponse(ERROR, traceId, timestamp, requestId)
