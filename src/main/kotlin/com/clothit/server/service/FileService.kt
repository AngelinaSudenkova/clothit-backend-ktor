package com.clothit.server.service

import io.ktor.http.content.*

interface FileService {
    suspend fun save(file: MultiPartData) : Int


}
