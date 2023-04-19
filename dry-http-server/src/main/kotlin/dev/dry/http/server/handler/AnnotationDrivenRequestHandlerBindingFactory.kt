package dev.dry.http.server.handler

import dev.dry.common.function.Either
import dev.dry.common.function.map
import dev.dry.common.io.ObjectReader
import dev.dry.common.io.read
import dev.dry.dependency.DependencyResolver
import dev.dry.http.Status
import dev.dry.http.annotation.AuthenticatedPrincipal
import dev.dry.http.annotation.Operation
import dev.dry.http.annotation.PathVariable
import dev.dry.http.annotation.QueryParameter
import dev.dry.http.annotation.RequestBody
import dev.dry.http.annotation.RequiredRoles
import dev.dry.http.server.ResponseFactory
import dev.dry.http.server.UriPath
import dev.dry.http.server.handler.binding.RequestHandlerBinding
import dev.dry.http.server.handler.binding.RequestHandlerOperationBinding
import dev.dry.http.server.parameter.AuthenticatedPrincipalResolver
import dev.dry.http.server.parameter.ParameterResolver
import dev.dry.http.server.parameter.PathVariableResolver
import dev.dry.http.server.parameter.QueryParameterResolver
import dev.dry.http.server.parameter.RequestBodyResolver
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType

typealias ParameterValueMapper = (String) -> Any

class AnnotationDrivenRequestHandlerBindingFactory(private val responseFactory: ResponseFactory) {
    sealed class RequestHandlerBindingError(val message: String)
    class RequestParameterUnmapped : RequestHandlerBindingError("request parameter unmapped to operation")

    companion object {
        private val logger = LoggerFactory.getLogger(AnnotationDrivenRequestHandlerBindingFactory::class.java)

        private val parameterValueMapperByType = mapOf<Class<*>, ParameterValueMapper>(
            String::class.java to { it },
            Int::class.java to { it.toInt() },
            LocalDate::class.java to { LocalDate.parse(it) },
        )

        inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? {
            return enumValues<T>().find { it.name == name }
        }

        private fun parameterValueMapper(type: KType, objectReader: ObjectReader): ParameterValueMapper {
            val javaType = type.javaType as Class<*>
            val kClass = type.classifier as KClass<*>
            if (kClass.isValue) {
                val ctor = kClass.primaryConstructor
                val parameter = ctor?.parameters?.firstOrNull()
                if (parameter != null) {
                    val valueType = (parameter.type.javaType as Class<*>)
                    val mapper = parameterValueMapperByType[valueType]
                    if (mapper != null) {
                        return { ctor.call(mapper(it)) }
                    }
                }
            }
            if (kClass.isSubclassOf(Enum::class)) {
                return { name ->
                    kClass.java.enumConstants.firstOrNull { (it as Enum<*>).name == name }
                        ?: throw IllegalStateException("enum instance of ${kClass.qualifiedName} not found with name $name")
                }
            }
            return parameterValueMapperByType[javaType] ?: { objectReader.read(it, javaType) }
        }

        internal fun findOperations(handlerClass: KClass<out Any>): List<Pair<KFunction<*>, Operation>> {
            return handlerClass.functions.mapNotNull { function ->
                val operation: Operation? = function.findAnnotation()
                if (operation != null) function to operation else null
            }
        }

        private fun mapOperation(
            handler: Any,
            function: KFunction<*>,
            operation: Operation,
            responseFactory: ResponseFactory,
            objectReader: ObjectReader,
            resolver: DependencyResolver,
        ): Either<RequestHandlerBindingError, RequestHandlerOperationBinding> {
            return mapParameterResolvers(function.parameters, objectReader, resolver).map { parameterResolvers ->
                val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
                val validator: Validator = factory.validator

                val requestHandler = DelegatingRequestHandler(
                    instance = handler,
                    function = function,
                    parameterResolvers = parameterResolvers,
                    validator = validator,
                    defaultStatus = Status.OK,
                    responseFactory = responseFactory,
                )

                val requiredRoles = function.findAnnotation<RequiredRoles>()?.roles?.toList() ?: emptyList()

                RequestHandlerOperationBinding(
                    method = operation.method,
                    path = UriPath(operation.path),
                    requiredRoles = requiredRoles,
                ) { requestHandler }
            }
        }

        internal fun mapParameterResolvers(
            parameters: List<KParameter>,
            objectReader: ObjectReader,
            resolver: DependencyResolver,
        ): Either<RequestHandlerBindingError, List<ParameterResolver>> {
            val parameterResolvers = ArrayList<ParameterResolver>(parameters.size)
            for ((index, parameter) in parameters.withIndex()) {
                if (parameter.kind == KParameter.Kind.INSTANCE || parameter.kind == KParameter.Kind.EXTENSION_RECEIVER) {
                    //parameterResolvers.add(InstanceParameterResolver(instance))
                    continue
                }

                val parameterValueClass = parameter.type

                val pathVariable: PathVariable? = parameter.findAnnotation()
                if (pathVariable != null) {
                    /*if (!pathVariable.required) {
                        !parameter.isOptional || !parameter.type.isMarkedNullable
                    }*/
                    parameter.type
                    val parameterResolver = PathVariableResolver(
                        parameterName = parameterName(index, parameter, pathVariable.name),
                        required = pathVariable.required,
                        parameterValueMapper = parameterValueMapper(parameterValueClass, objectReader)
                    )
                    parameterResolvers.add(parameterResolver)
                    continue
                }

                val queryParameter: QueryParameter? = parameter.findAnnotation()
                if (queryParameter != null) {
                    val parameterResolver = QueryParameterResolver(
                        parameterName = parameterName(index, parameter, queryParameter.name),
                        required = queryParameter.required,
                        parameterValueMapper = parameterValueMapper(parameterValueClass, objectReader)
                    )
                    parameterResolvers.add(parameterResolver)
                    continue
                }

                val requestBody: RequestBody? = parameter.findAnnotation()
                if (requestBody != null) {
                    val requestBodyClass = parameter.type.javaType as Class<*>
                    val parameterResolver = RequestBodyResolver(
                        parameterName = parameterName(index, parameter, null),
                        requestBodyClass,
                        objectReader,
                    )
                    parameterResolvers.add(parameterResolver)
                    continue
                }

                val authenticatedPrincipal: AuthenticatedPrincipal? = parameter.findAnnotation()
                if (authenticatedPrincipal != null) {
                    val parameterResolver = AuthenticatedPrincipalResolver(
                        parameterName = parameterName(index, parameter, null),
                        required = authenticatedPrincipal.required,
                        requestAttributes = resolver.resolve(),
                        factory = resolver.resolve(),
                    )
                    parameterResolvers.add(parameterResolver)
                    continue
                }

                return Either.left(RequestParameterUnmapped())
            }
            return Either.right(parameterResolvers)
        }

        private fun parameterName(index: Int, parameter: KParameter, name: String?): String {
            return (name ?: "").trim().ifEmpty { parameter.name ?: "parameter@$index" }
        }
    }

    fun constructBinding(
        handler: Any,
        resolver: DependencyResolver,
    ): Either<List<RequestHandlerBindingError>, RequestHandlerBinding> {
        val instanceClass = handler::class
        val errors = mutableListOf<RequestHandlerBindingError>()
        val operations = mutableListOf<RequestHandlerOperationBinding>()
        val objectReader: ObjectReader = resolver.resolve()
        for ((function, operation) in findOperations(instanceClass)) {
            mapOperation(handler, function, operation, responseFactory, objectReader, resolver)
                .fold(errors::add, operations::add)
        }

        return if (errors.isNotEmpty()) {
            val errorMessages = errors.joinToString { it.message }
            logger.error(
                "failed to construct request handler bindings for ${instanceClass.qualifiedName} [$errorMessages]"
            )
            Either.left(errors)
        } else {
            Either.right(RequestHandlerBinding(operations))
        }
    }
}
