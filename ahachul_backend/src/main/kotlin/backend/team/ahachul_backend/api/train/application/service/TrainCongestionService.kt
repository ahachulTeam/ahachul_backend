package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetCongestionDto
import backend.team.ahachul_backend.common.client.TrainCongestionClient
import backend.team.ahachul_backend.common.client.dto.TrainCongestionDto
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.stereotype.Service

@Service
class TrainCongestionService(
    private val trainCongestionClient: TrainCongestionClient
) {

    fun getTrainCongestion(subwayLine: Int, trainNo: Int): GetCongestionDto.Response {
        if (isInValidSubwayLine(subwayLine)) {
            throw BusinessException(ResponseCode.INVALID_SUBWAY_LINE)
        }
        val response = trainCongestionClient.getCongestions(subwayLine, trainNo)
        val trainCongestion = response.data!!
        val congestions = mapCongestionDto(response.success, trainCongestion)

        return GetCongestionDto.Response(
            trainNo = trainCongestion.trainY.toInt(),
            congestions = congestions
        )
    }

    private fun isInValidSubwayLine(subwayLine: Int): Boolean {
        return !ALLOWED_SUBWAY_LINE.contains(subwayLine)
    }

    private fun mapCongestionDto(success: Boolean, trainCongestion: TrainCongestionDto.Train
    ): List<GetCongestionDto.Section>  {
        var congestion = listOf<GetCongestionDto.Section>()
        if (success) {
            val congestionList = parse(trainCongestion.congestionResult.congestionCar)
            congestion = congestionList.mapIndexed {
                    idx, it -> GetCongestionDto.Section.from(idx + 1, it)
            }
        }
        return congestion
    }

    private fun parse(congestion: String): List<Int> {
        val congestions = congestion.trim().split(DELIMITER)
        return congestions.map { it.toInt() }
    }

    companion object {
        val ALLOWED_SUBWAY_LINE = listOf(2, 3)
        const val DELIMITER = "|"
    }
}
