package backend.team.ahachul_backend.common.utils

import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.schedule.UpdateLostDataJob
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.io.FileReader
import java.io.IOException

@Component
class FileUtils {

    companion object {
        val objectMapper = ObjectMapper()

        inline fun <reified T: Any> readFileData(): T = run {
            try {
                FileReader(UpdateLostDataJob.FILE_READ_PATH).use {
                    val typeRef = object : TypeReference<T>() {}
                    return objectMapper.readValue(it, typeRef)
                }
            } catch (e: IOException) {
                throw CommonException(ResponseCode.INTERNAL_SERVER_ERROR)
            }
        }
    }
}