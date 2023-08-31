package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.common.application.port.out.StationReader
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetCongestionDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto
import backend.team.ahachul_backend.common.client.RedisClient
import backend.team.ahachul_backend.common.client.TrainCongestionClient
import backend.team.ahachul_backend.common.client.dto.TrainCongestionDto
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.response.ResponseCode
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class TrainCongestionService(
    private val trainCongestionClient: TrainCongestionClient,
    private val stationReader: StationReader,
    private val redisClient: RedisClient,
    private val objectMapper: ObjectMapper
) {

    fun getTrainCongestion(stationId: Long): GetCongestionDto.Response {
        val station = stationReader.getById(stationId)
        val subwayLine = station.subwayLine

        if (isInValidSubwayLine(subwayLine.id)) {
            throw BusinessException(ResponseCode.INVALID_SUBWAY_LINE)
        }

        val cachedTrain = getCachedTrain(subwayLine.identity, station.id)!!
        val trainNo = extractLatestTrainNo(cachedTrain)
        val response = trainCongestionClient.getCongestions(subwayLine.id, trainNo)
        val trainCongestion = response.data!!
        val congestions = mapCongestionDto(response.success, trainCongestion)

        return GetCongestionDto.Response(
            trainNo = trainCongestion.trainY.toInt(),
            congestions = congestions
        )
    }

    private fun isInValidSubwayLine(subwayLine: Long): Boolean {
        return !ALLOWED_SUBWAY_LINE.contains(subwayLine)
    }

    private fun getCachedTrain(subwayLineIdentity: Long, stationId: Long): List<GetTrainRealTimesDto.TrainRealTime>? {
        val key = "${TrainService.TRAIN_REAL_TIME_REDIS_PREFIX}${subwayLineIdentity}-$stationId"
        val cachedData = redisClient.get(key)

        return cachedData?.let {
            val typeRef = object : TypeReference<List<GetTrainRealTimesDto.TrainRealTime>>() {}
            objectMapper.readValue(it, typeRef)
        }
    }

    private fun extractLatestTrainNo(trainRealTimes: List<GetTrainRealTimesDto.TrainRealTime>): Int {
        val sortedTrains = trainRealTimes.sortedWith(compareBy(
            { it.currentTrainArrivalCode.priority },
            { it.stationOrder }
        ))
        return sortedTrains[0].trainNum.toInt()
    }

    private fun mapCongestionDto(
        success: Boolean, trainCongestion: TrainCongestionDto.Train
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
        val ALLOWED_SUBWAY_LINE = listOf(2L, 3L)
        const val DELIMITER = "|"
    }
}
