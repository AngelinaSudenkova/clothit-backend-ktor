package com.clothit.server.dao.impl

import com.clothit.server.dao.UserDao
import com.clothit.server.model.entity.UserEntity
import com.clothit.server.model.persistence.UserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

object UserDaoImpl : UserDao {
    override fun save(entity: UserEntity): UUID {
        val id = UUID.randomUUID()
        transaction {
            UserTable.insert {
                it[UserTable.id] = id
                it[email] = entity.email
                it[passwordHash] = entity.passwordHash
                it[username] = entity.username
                it[registeredDate] = entity.registeredDate
                it[lastLoginDate] = entity.lastLoginDate
                it[isActive] = entity.isActive
            }
        }
        return id
    }

    override fun getById(id: UUID): UserEntity? {
        return transaction {
            UserTable.select { UserTable.id eq id }
                .mapNotNull {
                    UserEntity(
                        it[UserTable.id],
                        it[UserTable.email],
                        it[UserTable.passwordHash],
                        it[UserTable.username],
                        it[UserTable.registeredDate],
                        it[UserTable.lastLoginDate],
                        it[UserTable.isActive]
                    )
                }
                .singleOrNull()
        }
    }

    override fun searchByEmail(email: String): UserEntity? {
        return transaction {
            UserTable.select { UserTable.email eq email }
                .mapNotNull {
                    UserEntity(
                        it[UserTable.id],
                        it[UserTable.email],
                        it[UserTable.passwordHash],
                        it[UserTable.username],
                        it[UserTable.registeredDate],
                        it[UserTable.lastLoginDate],
                        it[UserTable.isActive]
                    )
                }
                .singleOrNull()
        }
    }

    override fun update(entity: UserEntity): String {
        transaction {
            UserTable.update({ UserTable.id eq entity.id!! }) {
                it[email] = entity.email
                it[passwordHash] = entity.passwordHash
                it[username] = entity.username
                it[registeredDate] = entity.registeredDate
                it[lastLoginDate] = entity.lastLoginDate
                it[isActive] = entity.isActive
            }
        }
        return entity.id.toString()
    }

    override fun delete(id: UUID): Boolean {
        var deleted = false
        transaction {
            val rowsDeleted = UserTable.deleteWhere { UserTable.id eq id }
            deleted = rowsDeleted > 0
        }
        return deleted
    }

    override fun searchByUsername(name: String): List<UserEntity> {
        return transaction {
            UserTable.select { UserTable.username like "%$name%" }
                .map {
                    UserEntity(
                        it[UserTable.id],
                        it[UserTable.email],
                        it[UserTable.passwordHash],
                        it[UserTable.username],
                        it[UserTable.registeredDate],
                        it[UserTable.lastLoginDate],
                        it[UserTable.isActive]
                    )
                }
        }
    }
}
