package dev.dry.data.model.type

import dev.dry.common.model.value.IsoCountryCode

@JvmInline
value class MobileNumber(val value: String) {
    interface Parser {
        fun parse(value: String, defaultCountry: IsoCountryCode): MobileNumber?
    }
}
