package com.clothit.server.dao.impl

import com.clothit.server.dao.TokenDao
import com.clothit.server.model.entity.TokenEntity
import com.clothit.server.model.persistence.TokenTable
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.util.*

object TokenDaoImpl : TokenDao {

    override fun save(token: TokenEntity): Int {
        var id = -1
        transaction {
            id = TokenTable.insert {
                it[userId] = token.userId
                it[this.token] = token.token
            } get TokenTable.id
        }
        return id
    }

    override fun getTokenByUserId(userId: UUID): TokenEntity? {
        return transaction {
            TokenTable.select { TokenTable.userId eq userId }
                .map {
                    TokenEntity(
                        it[TokenTable.id],
                        it[TokenTable.userId],
                        it[TokenTable.token]
                    )
                }.singleOrNull()
        }
    }

    override fun deleteTokenByUserId(userId: UUID) {
        transaction {
            TokenTable.deleteWhere { TokenTable.userId eq userId }
        }
    }

    override fun deleteToken(token: String) {
        transaction {
            TokenTable.deleteWhere { TokenTable.token eq token }
        }
    }

    override fun tokenExists(credential: JWTCredential): Boolean {

        val userId = credential.payload.getClaim("userId").asString()
        val logger = LoggerFactory.getLogger("Application")
        logger.error(userId)
        return transaction {
            TokenTable.select { TokenTable.userId eq UUID.fromString(userId)}.empty().not()
        }
    }
}

