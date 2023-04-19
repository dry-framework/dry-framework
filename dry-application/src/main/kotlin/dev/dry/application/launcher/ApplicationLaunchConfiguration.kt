package dev.dry.application.launcher

import dev.dry.application.module.ApplicationModuleLoader
import dev.dry.application.module.DefaultApplicationModuleLoader
import dev.dry.common.exception.SystemRuntimeException
import dev.dry.dependency.DefaultDependencyNameResolver
import dev.dry.dependency.DependencyRegistry
import java.util.regex.*

class ApplicationLaunchConfiguration(
    val applicationName: String,
    /**
     * prefix for environment variable names that are loaded into configuration properties -- defaults to APP.
     *  A trailing underscore is automatically added.
     */
    val environmentVariableNamePrefix: String,
    val applicationModuleLoader: ApplicationModuleLoader = DefaultApplicationModuleLoader,
    val dependencyRegistry: DependencyRegistry = DependencyRegistry(DefaultDependencyNameResolver)
) {
    companion object {
        private val APPLICATION_NAME_VALIDATION_PREDICATE = Pattern
            .compile("^[a-z]+(-[a-z]+)*\$")
            .asMatchPredicate()
        private val ENVIRONMENT_VARIABLE_NAME_PREFIX_VALIDATION_PREDICATE = Pattern
            .compile("^[A-Z]+(_[A-Z]+)*\$")
            .asMatchPredicate()
    }

    init {
        if (!APPLICATION_NAME_VALIDATION_PREDICATE.test(applicationName)) {
            throw SystemRuntimeException(
                "the application name must: " +
                    "consist solely of alphabetic characters and dashes, " +
                    "start and end with at least one alphabetic character, " +
                    "and not contain consecutive dashes."
            )
        }
        if (!ENVIRONMENT_VARIABLE_NAME_PREFIX_VALIDATION_PREDICATE.test(environmentVariableNamePrefix)) {
            throw SystemRuntimeException(
                "the environment variable name prefix must: " +
                    "consist solely of uppercase alphabetic characters or underscores, " +
                    "start and end with at least one alphabetic character, " +
                    "and not contain consecutive underscores."
            )
        }
    }
}
