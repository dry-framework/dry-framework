package dev.dry.http.server.handler

import dev.dry.common.io.ObjectReader
import dev.dry.dependency.DefaultDependencyNameResolver
import dev.dry.dependency.DependencyResolver
import dev.dry.http.Method.GET
import dev.dry.http.Method.POST
import dev.dry.http.annotation.Operation
import dev.dry.http.annotation.PathVariable
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.InputStream

class DelegatingRequestHandlerBindingFactoryTest {
    class TestRequest(val message: String = "Test Request")

    class TestResponse(val message: String = "Test Response")

    class TestController {
        @Operation(method = POST, path = "", summary = "operation with request body")
        fun operationWithRequestBody(requestBody: TestRequest): TestResponse = TestResponse()

        @Operation(method = GET, path = "", summary = "operation without parameters")
        fun operationWithoutParameters(): TestResponse = TestResponse()

        @Operation(method = GET, path = "", summary = "operation with path variable")
        fun operationWithPathVariable(@PathVariable id: String): TestResponse = TestResponse()

        operator fun invoke(@PathVariable id: String): TestResponse = TestResponse()

        fun nonAnnotatedFunction1(request: TestRequest): TestResponse = TestResponse()

        fun nonAnnotatedFunction2(): TestResponse = TestResponse()
    }

    class TestOperation {
        @Operation(method = GET, path = "", summary = "invoke operation")
        operator fun invoke(): TestResponse = TestResponse()

        fun nonAnnotatedFunction(): TestResponse = TestResponse()
    }

    class TestNonAnnotated {
        fun notAnOperation(): TestResponse = TestResponse()
    }

    companion object {
        val DUMMY_OBJECT_READER = object: ObjectReader {
            override fun <T : Any> read(input: InputStream, objectClass: Class<T>): T {
                throw RuntimeException("Test Dummy not implemented")
            }
        }

        val DEPENDENCY_RESOLVER = DependencyResolver(DefaultDependencyNameResolver, emptyMap())
    }

    @Test
    fun findMultipleOperations() {
        val operations = AnnotationDrivenRequestHandlerBindingFactory.findOperations(TestController::class)
        assertEquals(3, operations.size)
        val (_, operationWithoutParameters) = operations.firstOrNull { it.first.name == "operationWithoutParameters" }
            ?: fail("operation not found with name 'operationWithoutParameters'")
        assertEquals(GET, operationWithoutParameters.method)
        assertEquals("operation without parameters", operationWithoutParameters.summary)
        val (_, operationWithRequestBody) = operations.firstOrNull { it.first.name == "operationWithRequestBody" }
            ?: fail("operation not found with name 'operationWithRequestBody'")
        assertEquals(POST, operationWithRequestBody.method)
        assertEquals("operation with request body", operationWithRequestBody.summary)
        val (_, operationWithPathVariable) = operations.firstOrNull { it.first.name == "operationWithPathVariable" }
            ?: fail("operation not found with name 'operationWithPathVariable'")
        assertEquals(GET, operationWithPathVariable.method)
        assertEquals("operation with path variable", operationWithPathVariable.summary)
    }

    @Test
    fun findSingleOperation() {
        val operations = AnnotationDrivenRequestHandlerBindingFactory.findOperations(TestOperation::class)
        assertEquals(1, operations.size)
        val (_, invokeOperation) = operations.firstOrNull { it.first.name == "invoke" }
            ?: fail("operation not found with name 'invoke'")
        assertEquals(GET, invokeOperation.method)
        assertEquals("invoke operation", invokeOperation.summary)
    }

    @Test
    fun findOperationsOnNonAnnotatedClass() {
        val operations = AnnotationDrivenRequestHandlerBindingFactory.findOperations(TestNonAnnotated::class)
        assertTrue(operations.isEmpty())
    }

    @Test
    fun mapParameterResolvers() {
        val parameters = TestController::operationWithPathVariable.parameters
        val resolvers = AnnotationDrivenRequestHandlerBindingFactory.mapParameterResolvers(
            parameters,
            DUMMY_OBJECT_READER,
            DEPENDENCY_RESOLVER,
        )
        assertTrue(resolvers.isRight)
        assertEquals(1, resolvers.fold({ 0 }, { it.size }))
    }

    @Test
    fun mapParameterZeroResolvers() {
        val parameters = TestController::operationWithoutParameters.parameters
        val resolvers = AnnotationDrivenRequestHandlerBindingFactory.mapParameterResolvers(
            parameters,
            DUMMY_OBJECT_READER,
            DEPENDENCY_RESOLVER,
        )
        assertTrue(resolvers.isRight)
        assertTrue(resolvers.fold({ false }, { it.isEmpty() }))
    }
}
