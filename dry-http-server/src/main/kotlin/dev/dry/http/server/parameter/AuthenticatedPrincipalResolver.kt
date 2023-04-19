package dev.dry.http.server.parameter

import dev.dry.common.error.CodedError
import dev.dry.http.server.Request
import dev.dry.http.server.RequestAttributes
import dev.dry.http.server.error.HttpErrors
import org.slf4j.LoggerFactory
import java.security.Principal

class AuthenticatedPrincipalResolver(
    override val parameterName: String,
    override val required: Boolean,
    private val requestAttributes: RequestAttributes,
    private val factory: AuthenticatedPrincipalFactory,
) : ParameterResolver {
    override fun resolve(request: Request): Any? {
        return requestAttributes.principal(request) ?: if (required) handleError(HttpErrors.UNAUTHORIZED_ERROR) else null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthenticatedPrincipalResolver::class.java)

        fun handleError(error: CodedError): Principal? {
            logger.info("failed to resolve AuthenticatedPrincipal parameter - {}", error.exceptionMessage)
            throw error.toException()
        }
    }
}
