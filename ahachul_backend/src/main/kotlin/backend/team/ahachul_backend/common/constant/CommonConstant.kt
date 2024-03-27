package backend.team.ahachul_backend.common.constant

import java.time.format.DateTimeFormatter

class CommonConstant {
    companion object {
        // HASHTAG
        const val HASHTAG_REDIS_KEY = "hashtag_rank"
        const val LOST_FILE_URL = "/home/all.json"
        val HASHTAG_FILE_URL = "${System.getProperty("user.dir")}/logs/archive/hashtag/hashtag_log.log"
        val HASHTAG_LOG_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")
    }
}
