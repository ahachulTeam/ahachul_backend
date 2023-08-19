package backend.team.ahachul_backend.api.train.application.port.`in`

import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto

interface TrainUseCase {

    fun getTrain(trainNo: String): GetTrainDto.Response
}