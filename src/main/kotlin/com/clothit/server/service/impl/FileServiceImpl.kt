package com.clothit.server.service.impl

import com.clothit.server.dao.FileDao
import com.clothit.server.model.entity.FileEntity
import com.clothit.server.service.FileService
import com.clothit.util.FileUtil
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class FileServiceImpl(private val fileDao: FileDao) : FileService {

    override suspend fun save(file: MultiPartData): Int {
        var fileId = -1
        withContext(Dispatchers.IO){
            file.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val inputStream: InputStream = part.streamProvider()
                    val byteArray = inputStream.readBytes()
                    FileUtil.saveToFile(part.originalFileName!!, byteArray)
                    val fileEntity = FileEntity(
                        name = part.originalFileName ?: "Unknown",
                        size = byteArray.size.toLong(),
                        null
                    )
                    fileId = fileDao.save(fileEntity)

                }
            }
        }
        return fileId
    }

    override fun getById(fileId: Int): ByteArray {
        val fileEntity = fileDao.getById(fileId)
        val fileName = fileEntity.name
        return FileUtil.readFromFile(fileName)
    }
}
