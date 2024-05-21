package com.clothit.server.service.impl

import com.clothit.error.CustomException
import com.clothit.server.dao.FileDao
import com.clothit.server.model.entity.FileEntity
import com.clothit.server.service.FileService
import com.clothit.util.DateTimeUtil
import com.clothit.util.FileUtil
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class FileServiceImpl(private val fileDao: FileDao) : FileService {

    override suspend fun save(file: MultiPartData): Int {
        var fileId = -1
        try {
            withContext(Dispatchers.IO) {
                file.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val inputStream: InputStream = part.streamProvider()
                        val byteArray = inputStream.readBytes()
                        FileUtil.saveToFile(part.originalFileName!!, byteArray)
                        val fileEntity = FileEntity(
                            name = part.originalFileName ?: "Unknown",
                            size = byteArray.size.toLong(),
                        )
                        fileId = fileDao.save(fileEntity)

                    }
                }
            }
            return fileId
        } catch (e: CustomException.FileCanNotBeSavedException) {
            throw CustomException.FileCanNotBeSavedException()
        }
    }

    override fun getById(fileId: Int): ByteArray {
        try {
            val fileEntity = fileDao.getById(fileId)
            val fileName = fileEntity.name
            return FileUtil.readFromFile(fileName)
        } catch (e: Exception) {
           throw CustomException.FileNotFoundException()
        }
    }

    override suspend fun update(fileId: Int, newFile: MultiPartData) {
        try {
            val oldFileEntity = fileDao.getById(fileId)
            val oldFileName = oldFileEntity.name
            val oldByteArray = FileUtil.readFromFile(oldFileName)

            var newByteArray: ByteArray? = null
            withContext(Dispatchers.IO) {
                newFile.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val inputStream: InputStream = part.streamProvider()
                        newByteArray = inputStream.readBytes()
                        if (!oldByteArray.contentEquals(newByteArray)) {
                            FileUtil.saveToFile(part.originalFileName!!, newByteArray!!)
                            val fileEntity = FileEntity(
                                id = oldFileEntity.id!!,
                                item = oldFileEntity.item,
                                name = part.originalFileName ?: "Unknown",
                                size = newByteArray!!.size.toLong(),
                                timeCreated = oldFileEntity.timeCreated,
                                timeUpdated = DateTimeUtil.getCurrentTime()
                            )
                            fileDao.update(fileEntity)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw CustomException.UpdateException()
        }
    }
}

