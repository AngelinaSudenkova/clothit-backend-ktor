package com.clothit.server.service

import io.ktor.http.content.*

interface FileService {

    suspend fun save(file: MultiPartData) : Int

    fun getById(fileId: Int) : ByteArray

    suspend fun update(fileId: Int, newFile: MultiPartData)

}
