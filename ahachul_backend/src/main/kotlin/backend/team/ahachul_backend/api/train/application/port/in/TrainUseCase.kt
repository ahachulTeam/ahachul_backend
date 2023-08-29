package backend.team.ahachul_backend.api.train.application.port.`in`

import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto

interface TrainUseCase {

    fun getTrain(trainNo: String): GetTrainDto.Response

    fun getTrainRealTimes(stationId: Long): List<GetTrainRealTimesDto.TrainRealTime>
}