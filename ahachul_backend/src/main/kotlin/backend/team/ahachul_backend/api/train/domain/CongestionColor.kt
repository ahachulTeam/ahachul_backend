package backend.team.ahachul_backend.api.train.domain

import backend.team.ahachul_backend.common.exception.DomainException
import backend.team.ahachul_backend.common.response.ResponseCode


enum class CongestionColor(
    private val startPercent: Int,
    private val endPercent: Int
) {

    GREEN(0, 35),
    YELLOW(35, 100),
    ORANGE(100, 200),
    RED(200, Int.MAX_VALUE);

    companion object {
        fun from(percent: Int): String {
            return CongestionColor.values()
                .find { percent >= it.startPercent && percent < it.endPercent }?.name
                ?: throw DomainException(ResponseCode.INVALID_DOMAIN)
        }
    }
}
