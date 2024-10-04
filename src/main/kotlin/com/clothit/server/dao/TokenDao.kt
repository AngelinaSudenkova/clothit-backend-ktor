package com.clothit.server.dao

import com.clothit.server.model.entity.TokenEntity
import io.ktor.server.auth.jwt.*
import java.util.*

interface TokenDao {
    fun save(token: TokenEntity): Int
    fun getTokenByUserId(userId: UUID): TokenEntity?
    fun deleteTokenByUserId(userId: UUID)
    fun deleteToken(token: String)
    fun tokenExists(credential: JWTCredential): Boolean

}