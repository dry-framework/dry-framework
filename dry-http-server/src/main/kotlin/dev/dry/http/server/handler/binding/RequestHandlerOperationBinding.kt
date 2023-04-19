package dev.dry.http.server.handler.binding

import dev.dry.dependency.DependencyResolver
import dev.dry.http.Method
import dev.dry.http.server.UriPath
import dev.dry.http.server.handler.RequestHandler

interface RequestHandlerOperationBinding {
    val method: Method
    val path: UriPath
    val requiredRoles: List<String>
    val factory: (DependencyResolver) -> RequestHandler

    fun resolveRequestHandler(resolver: DependencyResolver): RequestHandler = factory.invoke(resolver)

    companion object {
        operator fun invoke(
            method: Method,
            path: UriPath,
            requiredRoles: List<String>,
            factory: (DependencyResolver) -> RequestHandler,
        ): RequestHandlerOperationBinding {
            return DefaultRequestHandlerOperationBinding(method, path, requiredRoles, factory)
        }
    }

    class DefaultRequestHandlerOperationBinding(
        override val method: Method,
        override val path: UriPath,
        override val requiredRoles: List<String>,
        override val factory: (DependencyResolver) -> RequestHandler,
    ) : RequestHandlerOperationBinding
}
