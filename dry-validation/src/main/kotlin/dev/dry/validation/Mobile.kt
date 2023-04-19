package dev.dry.validation

import dev.dry.common.model.value.IsoCountryCode
import jakarta.validation.Constraint
import jakarta.validation.Payload
import java.lang.annotation.Documented
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = [MobileValidator::class])
annotation class Mobile(
    val defaultCountry: IsoCountryCode,
    val countries: Array<IsoCountryCode> = [],
    val optional: Boolean = false,

    val message: String = "invalid mobile number",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
)
