package backend.team.ahachul_backend.api.rank.event

import java.time.LocalDateTime

data class HashTagSearchEvent(
    val hashTagName: String,
    val userId: String,
    val timestamp: LocalDateTime
) {
}
