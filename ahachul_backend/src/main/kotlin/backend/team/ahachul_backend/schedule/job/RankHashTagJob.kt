package backend.team.ahachul_backend.schedule.job

import backend.team.ahachul_backend.common.constant.CommonConstant.Companion.HASHTAG_LOG_DATETIME_FORMATTER
import backend.team.ahachul_backend.common.constant.CommonConstant.Companion.HASHTAG_REDIS_KEY
import backend.team.ahachul_backend.common.client.RedisClient
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.utils.LogAnalyzeUtils
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

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
    fun readHashTagLogFile(fileUrl: String): Map<String, Int> {
        val endTime = LocalDateTime.now()
        val startTime = endTime.minusMinutes(1)
        val map = mutableMapOf<String, Int>()

        try {
            RandomAccessFile(fileUrl, "r").use {
                val fileLength = it.length()
                var pointer = fileLength - 2

                while (pointer > START_OF_FILE) {
                    // 각 줄 앞으로 포인터 이동
                    while (it.read().toChar() != NEW_LINE && pointer != START_OF_FILE) {
                        it.seek(pointer)
                        pointer -= 1
                    }

                    if (pointer != START_OF_FILE) pointer += 2
                    it.seek(pointer)

                    val logStr = String(it.readLine().toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                    val (timestamp, name) = LogAnalyzeUtils.extractArgsFromLogStr(logStr, HASHTAG_PATTERN, listOf("timestamp, name"))
                    val date = LocalDateTime.parse(timestamp, HASHTAG_LOG_DATETIME_FORMATTER)

                    if (date.isBefore(endTime) && date.isAfter(startTime)) {
                        logger.debug("read data between $startTime ~ $endTime : $timestamp")
                        if (!map.containsKey(name)) {
                            map[name] = 1
                        } else {
                            map[name] = map[name]!! + 1
                        }
                    } else if (date.isBefore(startTime)) {
                        logger.debug("terminate extracting data between $startTime ~ $endTime : $timestamp")
                        break
                    }
                    pointer -= 3
                }
            }
        } catch (ex: FileNotFoundException) {
            logger.error("invalid file to read : $fileUrl")
        }
        return map
    }

    private fun sortAndSetCache(map: Map<String, Int>) {
        val sortedResult = map.toList()
                .sortedByDescending { it.second }  // O(NlogN)
                .map { it.first }

        redisClient.set(HASHTAG_REDIS_KEY, sortedResult, 5, TimeUnit.MINUTES)
    }

    companion object {
        const val NEW_LINE = '\n'
        const val START_OF_FILE = 0L
        const val HASHTAG_PATTERN = "(?<timestamp>.*) \\[(?<thread>.*)\\] \\[(?<logger>.*)\\] userId = (?<userId>.*) hashtag = (?<name>.*)"
    }
}

