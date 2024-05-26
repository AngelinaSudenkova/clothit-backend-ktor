package com.clothit.server.service.impl

import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.dao.UserDao
import com.clothit.server.dao.impl.UserDaoImpl
import com.clothit.server.model.entity.UserEntity
import com.clothit.server.service.UserService
import com.clothit.util.DateTimeUtil
import com.clothit.util.PasswordUtil
import java.util.*

class UserServiceImpl(private val userDao: UserDao = UserDaoImpl) : UserService {

    override fun registerUser(userRegisterReq: UserRegisterReq): UUID? {
        if(userDao.checkIfExists(userRegisterReq.email)) {return null}
        val hashedPassword = PasswordUtil.hashPassword(userRegisterReq.password)
        val userEntity = UserEntity(
            id = null,
            email = userRegisterReq.email,
            passwordHash = hashedPassword,
            username = userRegisterReq.username,
            registeredDate = DateTimeUtil.getCurrentTime(),
            lastLoginDate = DateTimeUtil.getCurrentTime(),
            isActive = true
        )
        return userDao.save(userEntity)
    }

    override fun authenticateUser(email: String, password: String): UserDto? {

        val userEntity = userDao.searchByEmail(email)
        if (userEntity != null &&  PasswordUtil.checkPassword(password, userEntity.passwordHash)) {
            return UserDto(
                id = userEntity.id!!,
                username = userEntity.username,
            )
        }
        return null
    }

    override fun getUser(userId: UUID): UserDto? {
        val userEntity = userDao.getById(userId)
        return if (userEntity != null) {
            userEntity.id?.let {
                UserDto(
                    id = it,
                    username = userEntity.username,
                )
            }
        } else {
            null
        }
    }


    override fun deleteUser(userId: UUID) {
        userDao.delete(userId)
    }

    override fun searchByUsername(name: String): List<UserDto> {
        val userEntities = userDao.searchByUsername(name)
        return userEntities.map {
            UserDto(
                id = it.id!!,
                username = it.username
            )
        }
    }

    override fun searchByEmail(email: String): UserEntity? {
        val userEntity = userDao.searchByEmail(email)
        if (userEntity != null) {
            return userEntity
        } else {
            return null
        }
    }
}
