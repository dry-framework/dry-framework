package dev.dry.validation

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
import dev.dry.common.exception.Exceptions
import dev.dry.common.model.value.IsoCountryCode
import dev.dry.data.model.type.MobileNumber
import org.slf4j.LoggerFactory

class MobileNumberParser : MobileNumber.Parser {
    companion object {
        private val logger = LoggerFactory.getLogger(MobileValidator::class.java)
        private val util = PhoneNumberUtil.getInstance()
    }

    override fun parse(value: String, defaultCountry: IsoCountryCode): MobileNumber? {
        return try {
            val mobileNumber = util.parse(value, defaultCountry.alpha2Code)
            MobileNumber(util.format(mobileNumber, INTERNATIONAL).replace(" ", ""))
        } catch (ex: Exception) {
            logger.error("parsing mobile number failed with error: {}", Exceptions.getMessageChain(ex))
            return null
        }
    }
}