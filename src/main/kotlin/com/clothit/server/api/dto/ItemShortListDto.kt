package com.clothit.server.api.dto

import kotlinx.serialization.Serializable


@Serializable
data class ItemShortListDto(
    val listShortItems: List<ItemShortDto>
){

}