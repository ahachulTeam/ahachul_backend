package backend.team.ahachul_backend.schedule.job

import backend.team.ahachul_backend.common.client.RedisClient
import backend.team.ahachul_backend.common.logging.Logger
import com.google.gson.Gson
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.RandomAccessFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Component
class RankHashTagJob(
    private val redisClient: RedisClient
): QuartzJobBean() {

    private val logger = Logger(javaClass)

    override fun executeInternal(context: JobExecutionContext) {
        val jobDataMap = context.jobDetail.jobDataMap
        val fileReadPath = jobDataMap.getString("FILE_READ_PATH")
        val hashtagCountMap = readHashTagLogFile(fileReadPath)
        sortAndSetCache(hashtagCountMap)
    }

    /**
     * 배치성 메서드
     * 일정 주기마다 파일에서 정보를 가져와서 순위를 매긴다.
     */
    fun readHashTagLogFile(baseDir: String): Map<String, Int> {
        val endTime = LocalDateTime.now()
        val startTime = endTime.minusMinutes(1)
        val hashtagFileUrl = baseDir + "${endTime.year}_${endTime.month}_hashtag_log"
        val map = mutableMapOf<String, Int>()
        val gson = Gson()

        try {
            RandomAccessFile(hashtagFileUrl, "r").use {
                val fileLength = it.length()
                var pointer = fileLength - 2

                while (pointer >= START_OF_FILE) {
                    // 각 줄 앞으로 포인터 이동
                    while (it.read().toChar() != NEW_LINE) {
                        it.seek(pointer)
                        pointer -= 1
                    }

                    val jsonStr = String(it.readLine().toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                    val data = gson.fromJson(jsonStr, HashTag::class.java)
                    val date = LocalDateTime.parse(data.timestamp, DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))

                    if (date.isBefore(endTime) && date.isAfter(startTime)) {
                        logger.info("read data between $startTime ~ $endTime : ${data.timestamp}")
                        if (!map.containsKey(data.name)) {
                            map[data.name] = 1
                        } else {
                            map[data.name] = map[data.name]!! + 1
                        }
                    } else if (date.isBefore(startTime)) {
                        logger.info("terminate extracting data between $startTime ~ $endTime : ${data.timestamp}")
                        break
                    }
                }
            }
        } catch (ex: IOException) {
            logger.error("failed to read file : $hashtagFileUrl")
        }
        return map
    }

    private fun sortAndSetCache(map: Map<String, Int>) {
        val sortedResult = map.toList()
                .sortedByDescending { it.second }  // O(NlogN)
                .map { it.first }

        redisClient.set("hashtag_rank", sortedResult, 5, TimeUnit.MINUTES)
    }

    data class HashTag(
        val timestamp: String,
        val name: String,
        val userId: Long
    )

    companion object {
        const val NEW_LINE = '\n'
        const val START_OF_FILE = 0
    }
}

