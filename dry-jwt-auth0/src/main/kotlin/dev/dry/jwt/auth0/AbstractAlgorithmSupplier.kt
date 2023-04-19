package dev.dry.jwt.auth0

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier

abstract class AbstractAlgorithmSupplier: AlgorithmSupplier {
    private var currentVerifier: JWTVerifier? = null

    override fun verifier(): JWTVerifier {
        if (currentVerifier == null) {
            currentVerifier = JWT.require(algorithm()).build()
        }
        return currentVerifier!!
    }
}
