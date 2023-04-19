package dev.dry.http.server.handler

import dev.dry.common.exception.Exceptions
import dev.dry.common.time.StopWatch
import dev.dry.http.Status
import dev.dry.http.server.Request
import dev.dry.http.server.Response
import dev.dry.http.server.ResponseFactory
import dev.dry.http.server.parameter.ParameterResolver
import jakarta.validation.Validator
import jakarta.validation.executable.ExecutableValidator
import org.slf4j.LoggerFactory
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

class DelegatingRequestHandler internal constructor(
    private val instance: Any,
    private val function: KFunction<*>,
    private val parameterResolvers: List<ParameterResolver>,
    private val validator: Validator,
    private val defaultStatus: Status,
    private val responseFactory: ResponseFactory,
) : RequestHandler {
    private val logger = LoggerFactory.getLogger(DelegatingRequestHandler::class.java)

    private val executableValidator: ExecutableValidator = validator.forExecutables()

    val delegateSimpleName: String get() = instance::class.java.simpleName

    override fun invoke(request: Request): Response {
        val stopWatch = StopWatch.start()
        val parameterValues: MutableList<Any?> = ArrayList(parameterResolvers.size)
        logger.info("resolving parameters")
        try {
            for (resolver in parameterResolvers) {
                val value = resolver.resolve(request)
                parameterValues.add(value)
            }
        } catch (ex: Exception) {
            logger.error(
                "error resolving parameters ({}ms) -- {}",
                stopWatch.elapsedTimeMillis,
                Exceptions.getMessageChain(ex)
            )
            return responseFactory.constructResponse(request, defaultStatus, ex)
        }

        logger.info("validating parameter constraints ({}ms)", stopWatch.elapsedTimeMillis)
        val violations = executableValidator.validateParameters(
            instance,
            function.javaMethod,
            parameterValues.toTypedArray()
        )
        if (violations.isNotEmpty()) {
            logger.info("validating parameter constraints failed ({}ms)", stopWatch.elapsedTimeMillis)
            return responseFactory.constructResponse(request, violations)
        }

        logger.info("validating parameter value constraints ({}ms)", stopWatch.elapsedTimeMillis)
        for (parameterValue in parameterValues) {
            if (parameterValue != null) {
                val parameterValueViolations = validator.validate(parameterValue)
                if (parameterValueViolations.isNotEmpty()) {
                    return responseFactory.constructResponse(request, parameterValueViolations)
                }
            }
        }

        logger.info("calling delegate ({}ms)", stopWatch.elapsedTimeMillis)
        val actualParams = listOf(instance) + parameterValues
        return try {
            val returnValue = function.call(*actualParams.toTypedArray())

            logger.info("calling delegate completed ({}ms)", stopWatch.elapsedTimeMillis)
            responseFactory.constructResponse(request, defaultStatus, returnValue)
        } catch(th: Throwable) {
            logger.info(
                "calling delegate failed ({}ms) - {}",
                stopWatch.elapsedTimeMillis,
                Exceptions.getMessageChain(th)
            )
            responseFactory.constructResponse(request, defaultStatus, th)
        }
    }
}
