package backend.team.ahachul_backend.api.train.domain.model

import backend.team.ahachul_backend.common.exception.DomainException
import backend.team.ahachul_backend.common.response.ResponseCode.INVALID_ENUM

enum class TrainArrivalCode(
    val code: String
) {
    ENTER("0"), ARRIVE("1"), DEPARTURE("2"), BEFORE_STATION_DEPARTURE("3"), BEFORE_STATION_ENTER("4"), BEFORE_STATION_ARRIVE("5"), RUNNING("99");

    companion object {
        fun from(code: String): TrainArrivalCode {
            return TrainArrivalCode.values().find { it.code == code }
                ?: throw DomainException(INVALID_ENUM)
        }
    }
}