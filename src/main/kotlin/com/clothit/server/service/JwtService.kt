package com.clothit.server.service

import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserRegisterReq
import io.ktor.server.auth.jwt.*

interface JwtService {
    fun createToken(loginReq: UserLoginReq): String?
    fun validateToken(credential: JWTCredential) : JWTPrincipal?
    fun createToken(registerReq: UserRegisterReq): String?
}