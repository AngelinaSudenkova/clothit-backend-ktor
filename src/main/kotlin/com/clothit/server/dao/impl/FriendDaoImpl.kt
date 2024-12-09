package com.clothit.server.dao.impl

import com.clothit.server.model.entity.FriendEntity
import com.clothit.server.model.enums.FriendApplicationStatus
import com.clothit.server.model.persistence.FriendTable
import com.example.server.dao.FriendDao
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object FriendDaoImpl : FriendDao {

    override fun save(entity: FriendEntity): Int? = transaction {
        FriendTable.insert {
            it[userIdOwner] = entity.userOwner
            it[userIdInvited] = entity.userInvited
            it[invitedTime] = entity.invitedTime
            it[status] = entity.status.name
        } get FriendTable.id
    }

    override fun getById(id: Int): FriendEntity? = transaction {
        FriendTable.select { FriendTable.id eq id }
            .map { rowToEntity(it) }
            .singleOrNull()
    }

    override fun getAll(): List<FriendEntity> = transaction {
        FriendTable.selectAll().map { rowToEntity(it) }
    }

    override fun getFriendsByUser(userId: UUID): List<FriendEntity> = transaction {
        FriendTable.select { FriendTable.userIdOwner eq userId }
            .map { rowToEntity(it) }
    }

    override fun getFriendsByStatus(userId: UUID, status: FriendApplicationStatus): List<FriendEntity> = transaction {
        FriendTable.select {
            (FriendTable.userIdOwner eq userId) and (FriendTable.status eq status.name)
        }.map { rowToEntity(it) }
    }

    override fun getFriendRequest(senderId: String, recipientId: String): FriendEntity? {
        return transaction {
            val result = FriendTable.select {
                (FriendTable.userIdOwner eq UUID.fromString(senderId)) and
                        (FriendTable.userIdInvited eq UUID.fromString(recipientId)) and
                        (FriendTable.status eq FriendApplicationStatus.PENDING.name)
            }.singleOrNull()

            result?.let {
                rowToEntity(it)
            }
        }
    }

    override fun update(entity: FriendEntity): Unit = transaction {
        FriendTable.update({ FriendTable.id eq entity.id!! }) {
            it[userIdOwner] = entity.userOwner
            it[userIdInvited] = entity.userInvited
            it[invitedTime] = entity.invitedTime
            it[status] = entity.status.name
        }
    }

    override fun updateStatus(id: Int, status: FriendApplicationStatus): Unit = transaction {
        FriendTable.update({ FriendTable.id eq id }) {
            it[FriendTable.status] = status.name
        }
    }

    override fun delete(id: Int): Unit = transaction {
        val friend = FriendTable.select { FriendTable.id eq id }.singleOrNull()
        if (friend != null) {
            val ownerId = friend[FriendTable.userIdOwner]
            val invitedId = friend[FriendTable.userIdInvited]

            FriendTable.deleteWhere { FriendTable.id eq id }


            FriendTable.deleteWhere {
                (FriendTable.userIdOwner eq invitedId) and (FriendTable.userIdInvited eq ownerId)

            }
        }
    }


    private fun rowToEntity(row: ResultRow): FriendEntity {
        return FriendEntity(
            id = row[FriendTable.id],
            userOwner = row[FriendTable.userIdOwner],
            userInvited = row[FriendTable.userIdInvited],
            invitedTime = row[FriendTable.invitedTime] ,
            status = FriendApplicationStatus.valueOf(row[FriendTable.status])
        )
    }

    override fun deleteSentRequest(senderId: String, recipientId: String): Unit = transaction {
        FriendTable.deleteWhere {
            (FriendTable.userIdOwner eq UUID.fromString(senderId)) and
                    (FriendTable.userIdInvited eq UUID.fromString(recipientId)
                            and (FriendTable.status eq FriendApplicationStatus.SENT.name))
        }
    }
}