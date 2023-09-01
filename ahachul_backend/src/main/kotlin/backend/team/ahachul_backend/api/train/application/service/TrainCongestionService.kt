package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.common.application.port.out.StationReader
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetCongestionDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto
import backend.team.ahachul_backend.common.client.TrainCongestionClient
import backend.team.ahachul_backend.common.client.dto.TrainCongestionDto
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.stereotype.Service

@Service
class TrainCongestionService(
    private val trainCongestionClient: TrainCongestionClient,
    private val stationReader: StationReader,
    private val trainCacheUtils: TrainCacheUtils,
    private val trainService: TrainService // TODO: 의존성 제거
) {

    fun getTrainCongestion(stationId: Long): GetCongestionDto.Response {
        val station = stationReader.getById(stationId)
        val subwayLine = station.subwayLine

        if (isInValidSubwayLine(subwayLine.id)) {
            throw BusinessException(ResponseCode.INVALID_SUBWAY_LINE)
        }
        
        val trains = getCachedTrainOrRequestExternal(subwayLine.identity, stationId)
        val latestTrainNo = trains[0].trainNum.toInt()
        val response = trainCongestionClient.getCongestions(subwayLine.id, latestTrainNo)
        val trainCongestion = response.data!!
        val congestions = mapCongestionDto(response.success, trainCongestion)

        return GetCongestionDto.Response(
            trainNo = trainCongestion.trainY.toInt(),
            congestions = congestions
        )
    }
    
    private fun getCachedTrainOrRequestExternal(
        subwayLineIdentity: Long, stationId: Long
    ): List<GetTrainRealTimesDto.TrainRealTime> {
        return trainCacheUtils.getCache(subwayLineIdentity, stationId)
            ?: trainService.getTrainRealTimes(stationId)
    }
    
    private fun isInValidSubwayLine(subwayLine: Long): Boolean {
        return !ALLOWED_SUBWAY_LINE.contains(subwayLine)
    }

    private fun mapCongestionDto(
        success: Boolean, trainCongestion: TrainCongestionDto.Train
    ): List<GetCongestionDto.Section>  {
        if (success) {
            val congestionList = parse(trainCongestion.congestionResult.congestionCar)
            return congestionList.mapIndexed {
                    idx, it -> GetCongestionDto.Section.from(idx + 1, it)
            }
        }
        return emptyList()
    }

    private fun parse(congestion: String): List<Int> {
        val congestions = congestion.trim().split(DELIMITER)
        return congestions.map { it.toInt() }
    }

    companion object {
        val ALLOWED_SUBWAY_LINE = listOf(2L, 3L)
        const val DELIMITER = "|"
    }
}
