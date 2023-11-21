package backend.team.ahachul_backend.api.train.adapter.`in`.dto

import backend.team.ahachul_backend.api.train.domain.model.TrainArrivalCode
import backend.team.ahachul_backend.api.train.domain.model.UpDownType
import com.fasterxml.jackson.annotation.JsonIgnore

class GetTrainRealTimesDto {

    data class Request(
        val stationId: Long,
        val subwayLineId: Long
    ) {
    }

    data class Response(
        val trainRealTimes: List<TrainRealTime>,
    ) {
    }

    data class TrainRealTime(
        @JsonIgnore
        val subwayId: String?,
        @JsonIgnore
        val stationOrder: Int?,
        val upDownType: UpDownType,
        val nextStationDirection: String,
        val destinationStationDirection: String,
        val trainNum: String,
        val currentLocation: String,
        val currentTrainArrivalCode: TrainArrivalCode,
    ) {
    }
}
