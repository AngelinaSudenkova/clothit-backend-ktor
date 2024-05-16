package com.clothit.server.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class IdDto(
    val id: Int
){
    @Override
    override fun toString(): String {
        return "id:$id"
    }
}
