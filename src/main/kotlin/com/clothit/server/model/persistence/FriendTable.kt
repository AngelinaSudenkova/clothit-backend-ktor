package com.clothit.server.model.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object FriendTable : Table("friend") {
    val id = integer("id").autoIncrement()
    val userIdOwner = uuid("user_id_owner")
    val userIdInvited = uuid("user_id_invited")
    val invitedTime = timestamp("invited_time")
    val status =  varchar("status", 20)

    override val primaryKey = PrimaryKey(id)
}