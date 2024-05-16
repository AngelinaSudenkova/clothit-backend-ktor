package com.clothit.server.model.persistence


import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp


object ItemTable : Table("items") {
    val id = integer("id").autoIncrement()
    val category = varchar("category", 15)
    val description = varchar("description", 255).nullable()
    val timeCreated = timestamp("time_created")

    override val primaryKey = PrimaryKey(id)
}