package com.clothit.server.service.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.dao.TokenDao
import com.clothit.server.model.entity.TokenEntity
import com.clothit.server.model.entity.UserEntity
import com.clothit.server.service.JwtService
import com.clothit.server.service.UserService
import com.clothit.util.PasswordUtil
import com.typesafe.config.ConfigFactory
import io.ktor.server.auth.jwt.*
import java.util.*

class JwtServiceImpl(
    private val userService: UserService,
    private val tokenDao: TokenDao
) : JwtService {

    val config = ConfigFactory.load()

    private val secret = config.getString("jwt.secret")
    private val issuer = config.getString("jwt.issuer")
    private val audience = config.getString("jwt.audience")

    val realm = config.getString("jwt.realm")

    val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()


    override fun createToken(loginReq: UserLoginReq): String? {
        val foundUser: UserEntity? = userService.searchByEmail(loginReq.email)
        if (foundUser != null && PasswordUtil.checkPassword(loginReq.password, foundUser.passwordHash)) {
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("email", loginReq.email)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
            tokenDao.save(
                TokenEntity(
                    token = token,
                    userId = foundUser.id!!
                )
            )
            return token

        } else {
            return null
        }
    }


    override fun createToken(registerReq: UserRegisterReq): String? {
        val foundUser: UserEntity? = userService.searchByEmail(registerReq.email)
        if (foundUser != null ) {
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("email", registerReq.email)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
            tokenDao.save(
                TokenEntity(
                    token = token,
                    userId = foundUser.id!!
                )
            )
            return token

        } else {
            return null
        }
    }


    override fun validateToken(credential: JWTCredential): JWTPrincipal? {
        val email: String? = extractEmail(credential)
        val foundUser: UserEntity? = email?.let(userService::searchByEmail)
        return foundUser?.let {
            if (audienceMatches(credential))
                JWTPrincipal(credential.payload)
            else
                null
        }
    }

    override fun deleteToken(token: String) {
        tokenDao.deleteToken(token)
    }


    private fun audienceMatches(
        credential: JWTCredential,
    ): Boolean =
        credential.payload.audience.contains(audience)

    private fun extractEmail(credential: JWTCredential): String? =
        credential.payload.getClaim("email").asString()
}
