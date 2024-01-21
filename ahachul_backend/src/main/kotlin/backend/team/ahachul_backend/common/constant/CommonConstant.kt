package backend.team.ahachul_backend.common.constant

import java.time.format.DateTimeFormatter

class CommonConstant {
    companion object {
        // HASHTAG
        const val HASHTAG_REDIS_KEY = "hashtag_rank"
        val HASHTAG_LOG_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")
    }
}
