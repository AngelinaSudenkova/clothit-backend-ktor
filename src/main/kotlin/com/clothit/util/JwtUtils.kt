package com.clothit.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.ktor.server.auth.jwt.*
import java.util.*

class JwtUtils {

    companion object {

        private val config: Config = ConfigFactory.load()

        private val secret: String = config.getString("jwt.secret")
        private val issuer: String = config.getString("jwt.issuer")
        private val audience: String = config.getString("jwt.audience")

        val realm: String = config.getString("jwt.realm")

        val jwtVerifier: JWTVerifier = JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

        fun generateAccessToken(refreshTokenId: Int): String {
            val jwt = JWT.create()
                .withSubject(refreshTokenId.toString())
                .withAudience(audience)
                .withIssuer(issuer)
                .withIssuedAt(Date(System.currentTimeMillis()))
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
            return "Bearer $jwt"
        }

        fun audienceMatches(credential: JWTCredential): Boolean =
            credential.payload.audience.contains(audience)

        fun extractEmail(credential: JWTCredential): String? =
            credential.payload.getClaim("email").asString()
    }


}