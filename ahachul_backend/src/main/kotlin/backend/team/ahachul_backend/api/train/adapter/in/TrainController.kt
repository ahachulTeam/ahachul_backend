package backend.team.ahachul_backend.api.train.adapter.`in`

import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.common.annotation.Authentication
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TrainController(
    private val trainUseCase: TrainUseCase,
) {

    @Authentication
    @GetMapping("/v1/trains/{trainNo}")
    fun getTrain(@PathVariable trainNo: String): CommonResponse<GetTrainDto.Response> {
        return CommonResponse.success(trainUseCase.getTrain(trainNo))
    }

    @Authentication
    @GetMapping("/v1/trains/real-times")
    fun getTrainRealTimes(request: GetTrainRealTimesDto.Request): CommonResponse<GetTrainRealTimesDto.Response> {
        val result = trainUseCase.getTrainRealTimes(request.subwayLineId, request.stationId)
        return CommonResponse.success(result)
    }
}