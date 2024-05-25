package com.clothit.server.model.entity

import java.util.*

data class TokenEntity(
    val id: Int?,
    val userId: UUID,
    val token: String
){
    constructor(userId: UUID, token: String) : this(null, userId = userId, token = token)
}
