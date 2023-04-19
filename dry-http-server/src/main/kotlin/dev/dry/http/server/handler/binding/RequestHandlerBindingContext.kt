package dev.dry.http.server.handler.binding

import dev.dry.dependency.DependencyRegistry
import dev.dry.dependency.DependencyResolver

fun DependencyRegistry.http(block: RequestHandlerBindingContext.() -> Unit) {
    block(RequestHandlerBindingContext(this))
}

class RequestHandlerBindingContext(private val registry: DependencyRegistry) {
    fun handler(factory: DependencyResolver.() -> Any) {
        registry.singleton(RequestHandlerBindingFactory(factory))
    }
}
