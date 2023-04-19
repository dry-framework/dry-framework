package dev.dry.jwt.auth0

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

interface AlgorithmSupplier {
    fun algorithm(): Algorithm
    fun verifier(): JWTVerifier
}
