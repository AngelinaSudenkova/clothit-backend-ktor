package com.clothit.server.service.impl

import com.clothit.error.ExceptionCustomMessage
import com.clothit.error.ExceptionTypes
import com.clothit.server.api.dto.AccessTokenDto
import com.clothit.server.dao.TokenDao
import com.clothit.server.dao.UserDao
import com.clothit.server.model.entity.TokenEntity
import com.clothit.server.model.entity.UserEntity
import com.clothit.server.service.TokenService
import com.clothit.util.JwtUtils
import com.clothit.util.JwtUtils.Companion.audienceMatches
import com.clothit.util.JwtUtils.Companion.extractEmail
import io.ktor.server.auth.jwt.*
import java.util.*

class TokenServiceImpl(
    private val userDao: UserDao,
    private val tokenDao: TokenDao
) : TokenService {


    override fun createAccessToken(userId: UUID, refreshToken: String): AccessTokenDto {
        val token = tokenDao.findTokenByValue(refreshToken)
        tokenValidate(userId, token)
        val result = JwtUtils.generateAccessToken(token.id!!)
        return AccessTokenDto(result)
    }

    override fun createRefreshToken(user: UserEntity): String {
        val token = UUID.randomUUID().toString()
        val tokenEntity = TokenEntity(token = token, userId = user.id!!)
        tokenDao.save(tokenEntity)
        return token
    }


    override fun validateAccessToken(credential: JWTCredential): JWTPrincipal? { //TODO revision and fix logic
        val email: String? = extractEmail(credential)
        val foundUser: UserEntity? = email?.let(userDao::getByEmail)
        return foundUser?.let {
            if (audienceMatches(credential) && (tokenDao.tokenExists(credential)))
                JWTPrincipal(credential.payload)
            else
                null
        }
    }

    override fun deleteToken(token: String) {
        tokenDao.deleteToken(token)
    }

    ///---------------------------------- utils
    private fun tokenValidate(userId: UUID, token: TokenEntity) {
        if (token.userId != userId) {
            throw ExceptionCustomMessage(ExceptionTypes.BAD_REQUEST).toException()
        }
    }

}