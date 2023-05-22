package backend.team.ahachul_backend.common.utils

import backend.team.ahachul_backend.common.schedule.UpdateLostDataJob
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.quartz.SchedulerException
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
                throw SchedulerException(e)
            }
        }
    }
}