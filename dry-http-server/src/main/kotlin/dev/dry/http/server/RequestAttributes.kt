package dev.dry.http.server

import dev.dry.http.RequestId
import dev.dry.common.concurrent.TraceId
import dev.dry.security.auth.Principal

interface RequestAttributes {
    fun principal(request: Request): Principal?

    fun principal(request: Request, principal: Principal)

    fun principalRoles(request: Request): Set<String>

    fun principalRoles(request: Request, roles: Set<String>)

    fun traceId(request: Request): TraceId

    fun requestId(request: Request): RequestId?

    open class DefaultRequestAttributes : RequestAttributes {
        override fun principal(request: Request): Principal? = request.attribute(PRINCIPAL_ATTRIBUTE_KEY)

        override fun principal(request: Request, principal: Principal) =
            request.attribute(PRINCIPAL_ATTRIBUTE_KEY, principal)

        override fun principalRoles(request: Request): Set<String> =
            request.attribute(PRINCIPAL_ROLES_ATTRIBUTE_KEY) ?: emptySet()

        override fun principalRoles(request: Request, roles: Set<String>) =
            request.attribute(PRINCIPAL_ROLES_ATTRIBUTE_KEY, roles)

        override fun traceId(request: Request): TraceId = request.attribute(TRACE_ID_ATTRIBUTE_KEY)
            ?: TraceId.newInstance().also { request.attribute(TRACE_ID_ATTRIBUTE_KEY, it) }

        override fun requestId(request: Request): RequestId? = request.attribute(REQUEST_ID_ATTRIBUTE_KEY)
            ?: request.header(REQUEST_ID_HEADER)
                ?.let(::RequestId)
                ?.also { request.attribute(REQUEST_ID_ATTRIBUTE_KEY, it) }

        companion object {
            const val REQUEST_ID_HEADER = "x-request-id"
            const val REQUEST_ID_ATTRIBUTE_KEY = "RequestAttributes.REQUEST_ID"
            const val TRACE_ID_ATTRIBUTE_KEY = "RequestAttributes.TRACE_ID"
            const val PRINCIPAL_ATTRIBUTE_KEY = "RequestAttributes.PRINCIPAL"
            const val PRINCIPAL_ROLES_ATTRIBUTE_KEY = "RequestAttributes.PRINCIPAL_ROLES"
        }
    }
}
