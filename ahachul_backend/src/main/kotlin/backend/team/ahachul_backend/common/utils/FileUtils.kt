package backend.team.ahachul_backend.common.utils

import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.response.ResponseCode
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.springframework.stereotype.Component
import java.io.FileReader
import java.io.IOException

@Component
object FileUtils {

    val logger = Logger(javaClass)
    val objectMapper = ObjectMapper()

    inline fun <reified T: Any> readFileData(readPath: String): T? = run {
        try {
            FileReader(readPath).use {
                val typeRef = object : TypeReference<T>() {}
                return objectMapper.readValue(it, typeRef)
            }
        } catch (e: MismatchedInputException) {
            logger.info("no data to read from file : $readPath")
            return null
        } catch (e: IOException) {
            throw CommonException(ResponseCode.FILE_READ_FAILED, e)
        }
    }
}
