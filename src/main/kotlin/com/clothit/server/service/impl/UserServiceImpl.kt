package com.clothit.server.service.impl

import com.clothit.error.ErrorCustomMessage
import com.clothit.error.ErrorTypes
import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.dto.UserFriendDto
import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.dao.UserDao
import com.clothit.server.model.entity.UserEntity
import com.clothit.server.service.FriendService
import com.clothit.server.service.JwtService
import com.clothit.server.service.UserService
import com.clothit.util.DateTimeUtil
import com.clothit.util.PasswordUtil
import java.util.*

class UserServiceImpl(
    private val userDao: UserDao,
    private val tokeService: JwtService,
    private val friendService: FriendService
) : UserService {

    override fun registerUser(userRegisterReq: UserRegisterReq): String {
        if (userDao.checkIfExists(userRegisterReq.email)) {
            throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        }
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
        val uuid = userDao.save(userEntity)
        val token = tokeService.createToken(userRegisterReq)
        return token
    }

    override fun authenticateUser(email: String, password: String): String {

        val userEntity = userDao.searchByEmail(email)
            ?: throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        if (PasswordUtil.checkPassword(password, userEntity.passwordHash)) {
            val userDto = UserDto(
                id = userEntity.id!!,
                username = userEntity.username,
            )
            return tokeService.createToken(UserLoginReq(
                email = email,
                password = password
            ))
        } else {
            throw ErrorCustomMessage(ErrorTypes.NOT_AUTHENTICATED).toException()
        }
    }


    override fun getUser(userId: UUID): UserEntity {
        val userEntity = userDao.getById(userId)
        if (userEntity == null) {
            throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        }
        return userEntity
    }


    override fun deleteUser(userId: UUID) {
        userDao.delete(userId)
    }

    override fun searchByUsername(name: String): List<UserDto> {
        val userEntities = userDao.searchByUsername(name)
        if (userEntities.isEmpty()) {
            throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        }
        return userEntities.map {
            UserDto(
                id = it.id!!,
                username = it.username
            )
        }
    }

    override fun searchFriendsByUsername(userId: UUID, name: String): List<UserFriendDto> {
        val userEntities = searchByUsername(name)
        if (userEntities.isEmpty()) {
            throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        }
        val friends = friendService.getFriendsByUser(userId.toString())
        return userEntities.map { userDto ->
            val friend = friends.find { it.userInvitedId == userDto.id.toString() }
            UserFriendDto(
                id = userDto.id,
                username = userDto.username,
                status = friend?.status ?: "NOT_FRIEND",
                friendshipId = friend?.id.toString() ?: ""
            )
        }
        }

    override fun getByEmail(email: String): UserEntity {
        val userEntity = userDao.searchByEmail(email)
        if (userEntity == null) {
            throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        }
        return userEntity
    }
}
