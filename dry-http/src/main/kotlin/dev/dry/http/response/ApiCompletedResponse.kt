package dev.dry.http.response

import dev.dry.http.RequestId
import dev.dry.common.concurrent.TraceId
import dev.dry.http.response.ApiResponseStatus.COMPLETED
import java.time.LocalDateTime

open class ApiCompletedResponse(
    timestamp: LocalDateTime,
    traceId: TraceId,
    requestId: RequestId? = null,
): ApiResponse(COMPLETED, traceId, timestamp, requestId)