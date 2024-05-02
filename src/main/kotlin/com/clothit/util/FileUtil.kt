package com.clothit.util


import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FileUtil {
    companion object {

        private const val BASE_DIRECTORY = "E:\\"

        fun saveToFile(filename: String, byteArray: ByteArray) {
            val file = File(BASE_DIRECTORY + filename)
            file.createNewFile()
           // file.parentFile.mkdirs()
            val outputStream = FileOutputStream(file)
            outputStream.write(byteArray)
            outputStream.close()
        }

        fun readFromFile(filename: String): ByteArray {
            val file = File(BASE_DIRECTORY + filename)
            val inputStream = FileInputStream(file)
            val byteArray = inputStream.readBytes()
            inputStream.close()
            return byteArray
        }
    }
}