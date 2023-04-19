package dev.dry.http.server.handler.binding

import dev.dry.dependency.DependencyResolver
import dev.dry.http.server.handler.AnnotationDrivenRequestHandlerBindingFactory

class RequestHandlerBindingFactory(
    private val factory: DependencyResolver.() -> Any,
) : (DependencyResolver) -> RequestHandlerBinding {
    override fun invoke(resolver: DependencyResolver): RequestHandlerBinding {
        return when (val requestHandler = factory(resolver)) {
            is RequestHandlerBinding -> requestHandler
            is RequestHandlerOperationBinding -> RequestHandlerBinding(requestHandler)
            else -> annotationDrivenRequestHandler(resolver, requestHandler)
        }
    }

    companion object {
        private fun annotationDrivenRequestHandler(
            resolver: DependencyResolver,
            requestHandler: Any,
        ): RequestHandlerBinding {
            val factory = AnnotationDrivenRequestHandlerBindingFactory(resolver.resolve())
            return factory.constructBinding(requestHandler, resolver).fold({
                throw IllegalStateException() // TODO("propogate errors")
            }, {
                it
            })
        }
    }
}
