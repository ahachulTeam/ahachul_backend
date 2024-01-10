package backend.team.ahachul_backend.common.utils

import backend.team.ahachul_backend.common.logging.Logger
import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path


class FileUtils {

    companion object {
        val logger = Logger(FileUtils::class.java)

        fun createFile(filePath: String) {
            val path = Path.of(filePath).normalize()
            try {
                if (!Files.exists(path)) {
                    Files.createFile(path)
                }
            } catch (ex: IOException) {
                logger.info("Failed to create file : $filePath")
            }
        }

        fun saveFile(filePath: String, data: List<Any>) {
            val gson = Gson()
            try {
                BufferedWriter(OutputStreamWriter(FileOutputStream(filePath, true), "UTF-8")).use {
                    data.map { data -> gson.toJson(data) }
                        .forEach { str ->
                            it.write(str)
                            it.newLine()
                        }
                }
            } catch (ex: IOException) {
                logger.info("Failed to write file : $filePath")
            }
        }
    }
}
