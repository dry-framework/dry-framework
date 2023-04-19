package dev.dry.jwt.claims

class StringArrayJwtClaim(val value: Array<String>): JwtClaim {
    constructor(value: List<String>): this(value.toTypedArray())
}
