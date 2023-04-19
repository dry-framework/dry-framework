package dev.dry.http.server

import dev.dry.common.error.CodedError
import dev.dry.common.error.CommonErrors
import dev.dry.common.error.ValidationError
import dev.dry.common.error.ValidationError.ValidationErrorMessage
import dev.dry.common.exception.CodedException
import dev.dry.common.function.Either
import dev.dry.common.io.ObjectWriter
import dev.dry.common.time.TimeProvider
import dev.dry.http.Status
import dev.dry.http.Status.BAD_REQUEST
import dev.dry.http.annotation.ResponseStatus
import dev.dry.http.response.ApiCompletedResponseWithContent
import dev.dry.http.response.ApiError
import dev.dry.http.response.ApiErrorResponse
import dev.dry.http.response.ApiResponse
import jakarta.validation.ConstraintViolation
import jakarta.validation.ElementKind.PARAMETER
import jakarta.validation.Path.ParameterNode
import java.io.ByteArrayOutputStream
import kotlin.reflect.full.findAnnotation

class ResponseFactory(
    private val objectWriter: ObjectWriter,
    private val timeProvider: TimeProvider,
    private val requestAttributes: RequestAttributes,
) {
    fun constructResponse(
        request: Request,
        defaultSuccessStatus: Status,
        content: Any?,
    ): Response {
        return when(val mappedContent = mapAny(content)) {
            is Response -> mappedContent
            else -> {
                val status = if (mappedContent == null) {
                    defaultSuccessStatus
                } else {
                    mappedContent::class.findAnnotation<ResponseStatus>()?.value ?: defaultSuccessStatus
                }
                val traceId = requestAttributes.traceId(request)
                val requestId = requestAttributes.requestId(request)
                val response = ApiCompletedResponseWithContent(
                    data = mappedContent,
                    timestamp = timeProvider.now(),
                    traceId = traceId,
                    requestId = requestId,
                )
                constructResponse(status, response)
            }
        }
    }

    fun constructResponse(request: Request, violations: Set<ConstraintViolation<*>>): Response {
        val error = ValidationError(
            validationErrorMessages = violations.map {
                ValidationErrorMessage(
                    name = it.propertyPath.mapNotNull { pathNode ->
                        if (pathNode.kind == PARAMETER && pathNode is ParameterNode) {
                            //pathNode.parameterIndex
                            pathNode.name
                        } else null
                    }.joinToString("."),
                    message = it.message,
                )
            },
            code = CommonErrors.BAD_REQUEST.code,
            message = "invalid request",
        )
        return constructResponse(request, BAD_REQUEST, error)
    }

    fun constructResponse(request: Request, error: CodedError): Response =
        constructResponse(request, mapApiError(error))

    fun constructResponse(request: Request, apiError: ApiError): Response {
        val traceId = requestAttributes.traceId(request)
        val requestId = requestAttributes.requestId(request)
        val apiErrorResponse = ApiErrorResponse(
            error = apiError,
            timestamp = timeProvider.now(),
            traceId = traceId,
            requestId = requestId,
        )
        val status = Status.from(apiError.code.category)
        return constructResponse(status, apiErrorResponse)
    }

    fun constructResponse(status: Status, apiResponse: ApiResponse): Response {
        val output = ByteArrayOutputStream(1024)
        objectWriter.write(output, apiResponse)
        return Response(
            status = status,
            headers = APPLICATION_JSON_HEADERS,
            bodyBytes = output.toByteArray(),
        )
    }

    companion object {
        private val APPLICATION_JSON_HEADER: Header = "content-type" to "application/json; charset=utf-8"
        private val APPLICATION_JSON_HEADERS: Headers = listOf(APPLICATION_JSON_HEADER)

        internal fun mapAny(value: Any?): Any? {
            return when (value) {
                is Either<*, *> -> value.fold(::mapAny, ::mapAny)
                is CodedError -> mapApiError(value)
                is CodedException -> mapApiError(value.error)
                is Throwable -> mapApiError(CommonErrors.SERVER_ERROR)
                is Response -> value
                else -> value
            }
        }

        internal fun mapApiError(error: CodedError): ApiError  {
            val validationErrorMessages = if (error is ValidationError) error.validationErrorMessages else null
            return ApiError(error, validationErrorMessages)
        }
    }
}
