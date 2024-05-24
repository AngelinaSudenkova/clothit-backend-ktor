package com.clothit.server.model.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object UserTable : Table("users") {
    val id = uuid("id").autoGenerate()
    val email = varchar("email", 50)
    val passwordHash = varchar("password_hash", 255)
    val username = varchar("username", 50)
    val registeredDate = timestamp("register_time")
    val lastLoginDate = timestamp("last_login")
    val isActive = bool("is_active")

    override val primaryKey = PrimaryKey(id)
}