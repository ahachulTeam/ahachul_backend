package backend.team.ahachul_backend.common.schedule

import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.persistence.SubwayLineReader
import backend.team.ahachul_backend.common.utils.FileUtils
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class UpdateLostDataJob(
    private val lostPostWriter: LostPostWriter,
    private val subwayLineReader: SubwayLineReader
): Job {

    private val logger: Logger = Logger(javaClass)

    companion object {
        const val DEV_FILE_READ_PATH = "/home/ec2-user/ahachul_data/datas/updated.json"
        const val FILE_READ_PATH = "../temp.json"
    }

    override fun execute(context: JobExecutionContext?) {
        logger.info("===== START JOB: [${this::class.simpleName}] =====")

        val response = FileUtils.readFileData<List<Map<String, LostDataDto>>>()
        response.forEach { data ->
            data.values.map {
                LostPostEntity(
                    title = it.title,
                    content = it.context,
                    lostType = LostType.LOST,
                    origin = LostOrigin.LOST112,
                    storageNumber = it.phone,
                    storage = it.storagePlace,
                    subwayLine = getSubwayLineEntity(it.receiptPlace),
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

    private fun getSubwayLineEntity(receivedPlace: String): SubwayLineEntity? {
        val subwayLineName = extractSubwayLine(receivedPlace)

        return runCatching {
            subwayLineReader.getSubwayLineByName(subwayLineName)
        }.onFailure {
            logger.info("===== Passing no valid subwayLine: $subwayLineName ====")
        }.getOrNull()
    }

    private fun extractSubwayLine(place: String): String {
        val res = StringBuilder()
        for (i: Int in place.length - 2 downTo 0) {
            if (place[i] == '(') break
            res.append(place[i])
        }
        return res.reverse().toString()
    }
}