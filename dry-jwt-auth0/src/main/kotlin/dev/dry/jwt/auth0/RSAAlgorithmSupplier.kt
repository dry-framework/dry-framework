package dev.dry.jwt.auth0

import com.auth0.jwt.algorithms.Algorithm
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

class RSAAlgorithmSupplier(publicKey: RSAPublicKey, privateKey: RSAPrivateKey): AbstractAlgorithmSupplier() {
    private var currentAlgorithm: Algorithm = Algorithm.RSA256(publicKey, privateKey)

    override fun algorithm(): Algorithm = currentAlgorithm
}
