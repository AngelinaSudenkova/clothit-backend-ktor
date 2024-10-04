package com.clothit.server.model.persistence

import org.jetbrains.exposed.sql.Table

object TokenTable: Table("tokens") {
    val id = integer("id").autoIncrement()
    val userId = uuid("user_id").references(UserTable.id)
    val token = varchar("token", 400)

    override val primaryKey = PrimaryKey(id)
}