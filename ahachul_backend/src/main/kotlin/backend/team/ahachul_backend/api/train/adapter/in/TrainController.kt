package backend.team.ahachul_backend.api.train.adapter.`in`

import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetCongestionDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.api.train.application.service.TrainCongestionService
import backend.team.ahachul_backend.common.annotation.Authentication
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TrainController(
    private val trainUseCase: TrainUseCase,
    private val trainCongestionService: TrainCongestionService
) {

    @Authentication
    @GetMapping("/v1/trains/{trainNo}")
    fun getTrain(@PathVariable trainNo: String): CommonResponse<GetTrainDto.Response> {
        return CommonResponse.success(trainUseCase.getTrain(trainNo))
    }

    @Authentication
    @GetMapping("/v1/trains/real-times/congestion")
    fun getCongestion(request: GetCongestionDto.Request): CommonResponse<GetCongestionDto.Response> {
        val result = trainCongestionService.getTrainCongestion(request.subwayLine, request.trainNo)
        return CommonResponse.success(result)
    }
}
