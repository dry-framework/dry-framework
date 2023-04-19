package dev.dry.http.server.handler.binding

class RequestHandlerBinding(val operations: List<RequestHandlerOperationBinding>) {
    constructor(requestHandler: RequestHandlerOperationBinding): this(listOf(requestHandler))
}