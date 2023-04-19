package dev.dry.validation

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType
import dev.dry.common.exception.Exceptions
import dev.dry.common.model.value.IsoCountryCode
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.slf4j.LoggerFactory
import java.util.Collections.*


class MobileValidator : ConstraintValidator<Mobile, String> {
    companion object {
        private val logger = LoggerFactory.getLogger(MobileValidator::class.java)
        private val util = PhoneNumberUtil.getInstance()
    }

    private var defaultCountry: IsoCountryCode = IsoCountryCode.AF
    private var countries: Set<IsoCountryCode> = emptySet()
    private var optional: Boolean = false

    override fun initialize(constraintAnnotation: Mobile) {
        defaultCountry = constraintAnnotation.defaultCountry
        countries = LinkedHashSet<IsoCountryCode>().also {
            it.add(defaultCountry)
            it.addAll(constraintAnnotation.countries)
        }
        optional = constraintAnnotation.optional
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return optional
        }

        val mobileNumber = try {
            util.parse(value, defaultCountry.alpha2Code)
        } catch (ex: Exception) {
            logger.error("parsing mobile number failed with error: {}", Exceptions.getMessageChain(ex))
            return false
        }

        if (util.isPossibleNumberForType(mobileNumber, PhoneNumberType.MOBILE)) {
            return false
        }

        return countries.any { util.isValidNumberForRegion(mobileNumber, it.alpha2Code) }
    }
}
