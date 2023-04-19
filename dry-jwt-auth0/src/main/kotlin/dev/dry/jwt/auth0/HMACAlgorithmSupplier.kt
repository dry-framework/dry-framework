package dev.dry.jwt.auth0

import com.auth0.jwt.algorithms.Algorithm

class HMACAlgorithmSupplier(private val secretProvider: () -> String): AbstractAlgorithmSupplier() {
    private var currentSecret: String? = null
    private var currentAlgorithm: Algorithm? = null

    override fun algorithm(): Algorithm {
        val secret = secretProvider()
        if (currentAlgorithm != null && secret == currentSecret) {
            return currentAlgorithm!!
        }
        val algorithm = Algorithm.HMAC256(secret)
        currentSecret = secret
        currentAlgorithm = algorithm
        return algorithm
    }
}