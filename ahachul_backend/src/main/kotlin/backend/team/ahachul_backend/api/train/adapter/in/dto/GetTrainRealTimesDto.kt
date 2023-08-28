package backend.team.ahachul_backend.api.train.adapter.`in`.dto

import backend.team.ahachul_backend.api.train.domain.model.TrainArrivalCode
import backend.team.ahachul_backend.api.train.domain.model.UpDownType

class GetTrainRealTimesDto {

    data class Request(
        val subwayLineId: Long,
        val stationId: Long,
    ) {
    }

    data class Response(
        val subwayLineId: Long,
        val stationId: Long,
        val trainRealTimes: List<TrainRealTime>,
    ) {
    }

    data class TrainRealTime(
        val upDownType: UpDownType,
        val nextStationDirection: String,
        val destinationStationDirection: String,
        val trainNum: String,
        val currentLocation: String,
        val currentTrainArrivalCode: TrainArrivalCode,
    ) {
    }
}
