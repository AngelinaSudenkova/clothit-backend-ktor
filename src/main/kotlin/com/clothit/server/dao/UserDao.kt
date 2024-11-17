package com.clothit.server.dao

import com.clothit.server.model.entity.UserEntity
import java.util.*

interface UserDao {
    fun save(entity: UserEntity) : UUID
    fun getById(id: UUID) : UserEntity?
    fun findById(id: UUID) : UserEntity
    fun update(entity: UserEntity) : String
    fun delete(id: UUID) : Boolean
    fun searchByUsername(name: String?): List<UserEntity>
    fun checkIfExists(email: String): Boolean
    fun unCheckIfExists(email: String)
    fun findByEmail(email: String): UserEntity
    fun getByEmail(email: String): UserEntity?

}