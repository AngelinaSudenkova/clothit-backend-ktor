package com.clothit.server.service

import com.clothit.server.api.dto.AccessTokenDto
import com.clothit.server.model.entity.UserEntity
import io.ktor.server.auth.jwt.*
import java.util.*

interface TokenService {

    fun createAccessToken(userId: UUID, refreshToken: String): AccessTokenDto
    fun createRefreshToken(user: UserEntity): String
    fun validateAccessToken(credential: JWTCredential): JWTPrincipal?
    fun deleteToken(token: String)

}