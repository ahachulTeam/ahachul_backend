package backend.team.ahachul_backend.schedule.job

import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.persistence.SubwayLineReader
import backend.team.ahachul_backend.common.utils.FileUtils
import backend.team.ahachul_backend.schedule.Lost112Data
import org.quartz.*
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit


@Component
class UpdateLostDataJob(
    private val lostPostWriter: LostPostWriter,
    private val subwayLineReader: SubwayLineReader
): Job {

    private val logger: Logger = Logger(javaClass)

    companion object {
        val RETRY_KEY = "${this::class.simpleName} execution count"
    }

    override fun execute(context: JobExecutionContext?) {
        try {
            val jobDataMap = context!!.jobDetail.jobDataMap
            updateRetryCount(jobDataMap)

            val fileReadPath = jobDataMap.getString("FILE_READ_PATH")
            val response = FileUtils.readFileData<List<Map<String, Lost112Data>>>(fileReadPath)
            saveLostPosts(response)
        }  catch (e: SchedulerException) {
            logger.info("Recoverable exception occur while scheduling: restarting job [${ context!!.jobDetail.key } ]")
            TimeUnit.MINUTES.sleep(1)
            throw JobExecutionException(true)
        }
    }

    private fun updateRetryCount(jobDataMap: JobDataMap) {
        if (jobDataMap.containsKey(RETRY_KEY)) {
            val cnt = jobDataMap.getInt(RETRY_KEY)
            jobDataMap.put(RETRY_KEY, cnt + 1)
        }
    }

    private fun saveLostPosts(response: List<Map<String, Lost112Data>>) {
        response.forEach { data ->
            data.values.map {
                LostPostEntity.ofLost112(it, getSubwayLineEntity(it.receiptPlace))
            }.forEach {
                lostPostWriter.save(it)
            }
        }
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
        if (!place.endsWith(')')) return place

        val res = StringBuilder()
        for (i: Int in place.length - 2 downTo 0) {
            if (place[i] == '(') break
            res.append(place[i])
        }
        return res.reverse().toString()
    }
}