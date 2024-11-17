package com.clothit.server.dao.impl

import com.clothit.error.ExceptionCustomMessage
import com.clothit.error.ExceptionTypes
import com.clothit.server.dao.UserDao
import com.clothit.server.model.entity.UserEntity
import com.clothit.server.model.persistence.UserTable
import com.clothit.util.ObjectUtils
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
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

    override fun findByEmail(email: String): UserEntity {
        val user: UserEntity? = getByEmail(email)
        ObjectUtils.checkNotNull(user)
        return user!!
    }

    override fun getByEmail(email: String): UserEntity? {
        return getUser { UserTable.email eq email }
    }

    override fun getById(id: UUID): UserEntity? {
        return getUser { UserTable.id eq id }
    }

    private fun getUser(condition: SqlExpressionBuilder.() -> Op<Boolean>): UserEntity? {
        return transaction {
            UserTable.selectAll().where { condition() }
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

    override fun findById(id: UUID): UserEntity {
        val user: UserEntity? = getById(id)
        ObjectUtils.checkNotNull(user)
        return user!!
    }



    override fun unCheckIfExists(email: String) {
        if (checkIfExists(email)) {
            throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()
        }

    }

    override fun checkIfExists(email: String): Boolean {
        return transaction {
            UserTable.selectAll().where { UserTable.email eq email }
                .count() > 0
        }
    }

    override fun update(entity: UserEntity): String {
        transaction {
            UserTable.update({UserTable.id eq entity.id!! }) {
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

    override fun searchByUsername(name: String?): List<UserEntity> {
        return transaction {
            UserTable.selectAll().where { UserTable.username like "%$name%" }
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
