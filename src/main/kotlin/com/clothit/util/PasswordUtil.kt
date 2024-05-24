package com.clothit.util

import org.springframework.security.crypto.bcrypt.BCrypt

object PasswordUtil {
    fun hashPassword(plainPassword: String): String {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt())
    }

    fun checkPassword(plainPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(plainPassword, hashedPassword)
    }
}