package com.clothit.server.dao

import com.clothit.server.model.entity.TokenEntity
import java.util.*

interface TokenDao {
    fun save(token: TokenEntity): Int
    fun getTokenByUserId(userId: UUID): TokenEntity?
    fun deleteTokenByUserId(userId: UUID)

}