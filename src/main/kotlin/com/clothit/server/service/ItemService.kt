package com.clothit.server.service

import com.clothit.server.api.req.ItemCreateReq

interface ItemService {

    fun save(req: ItemCreateReq): Int
}