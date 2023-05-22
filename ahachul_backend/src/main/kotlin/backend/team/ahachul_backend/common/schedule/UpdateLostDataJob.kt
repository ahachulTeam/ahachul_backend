package backend.team.ahachul_backend.common.schedule

import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.model.RegionType
import backend.team.ahachul_backend.common.persistence.SubwayLineWriter
import backend.team.ahachul_backend.common.response.ResponseCode
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import java.io.FileReader
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



@Component
class UpdateLostDataJob(
    private val lostPostWriter: LostPostWriter,
    private val subwayLineWriter: SubwayLineWriter,
    val objectMapper: ObjectMapper
): Job {

    private val logger: Logger = Logger(javaClass)
    private lateinit var tempSubway: SubwayLineEntity

    companion object {
        const val DEV_FILE_READ_PATH = "/home/ec2-user/ahachul_data/datas/updated.json"
        const val FILE_READ_PATH = "../temp.json"
    }

    @PostConstruct
    fun init() {
        tempSubway = subwayLineWriter.save(
            SubwayLineEntity(name = "임시 호선", regionType = RegionType.METROPOLITAN))
    }

    override fun execute(context: JobExecutionContext?) {
        logger.info("===== START JOB: [${this::class.simpleName}] =====")

        val response = readFileData<List<Map<String, LostDataDto>>>()
        response.forEach { data ->
            data.values.map {
                LostPostEntity(
                    title = it.title,
                    content = it.context,
                    lostType = LostType.LOST,
                    origin = LostOrigin.LOST112,
                    storageNumber = it.phone,
                    storage = it.storagePlace,
                    subwayLine = tempSubway,
                    pageUrl = it.page,
                    receivedDate = LocalDateTime.parse(it.getDate,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH시경"))
                )
            }.forEach {
                lostPostWriter.save(it)
            }
        }

        logger.info("===== END JOB: [${this::class.simpleName}] =====")
    }

    private inline fun <reified T: Any> readFileData(): T = run {
        try {
            FileReader(FILE_READ_PATH).use {
                val typeRef = object : TypeReference<T>() {}
                return objectMapper.readValue(it, typeRef)
            }
        } catch (e: IOException) {
            throw CommonException(ResponseCode.INTERNAL_SERVER_ERROR)
        }
    }
}