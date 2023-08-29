package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.common.application.port.out.StationReader
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.api.train.application.port.out.TrainReader
import backend.team.ahachul_backend.api.train.domain.entity.TrainEntity
import backend.team.ahachul_backend.api.train.domain.model.TrainArrivalCode
import backend.team.ahachul_backend.api.train.domain.model.UpDownType
import backend.team.ahachul_backend.common.client.RedisClient
import backend.team.ahachul_backend.common.client.SeoulTrainClient
import backend.team.ahachul_backend.common.dto.TrainRealTimeDto
import backend.team.ahachul_backend.common.exception.AdapterException
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.response.ResponseCode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
@Transactional(readOnly = true)
class TrainService(
    private val trainReader: TrainReader,
    private val stationLineReader: StationReader,

    private val seoulTrainClient: SeoulTrainClient,

    private val redisClient: RedisClient,

    private val objectMapper: ObjectMapper,
): TrainUseCase {

    private val logger: Logger = Logger(javaClass)

    companion object {
        const val TRAIN_REAL_TIME_REDIS_PREFIX = "TRAIN_TIME:"
        const val TRAIN_REAL_TIME_REDIS_EXPIRE_SEC = 25L
    }

    override fun getTrain(trainNo: String): GetTrainDto.Response {
        val (prefixTrainNo, location, organizationTrainNo) = decompositionTrainNo(trainNo)
        val train: TrainEntity
        try {
             train = trainReader.getTrain(prefixTrainNo)
        } catch (e: AdapterException) {
            logger.info("prefixTrainNo is no matching train. prefixTrainNo : {}".format(prefixTrainNo))
            throw BusinessException(ResponseCode.INVALID_PREFIX_TRAIN_NO)
        }

        return GetTrainDto.Response.of(
            train = train,
            location = location,
            organizationTrainNo = organizationTrainNo,
        )
    }

    private fun decompositionTrainNo(trainNo: String): Triple<String, Int, String> {
        return Triple(
            trainNo.dropLast(3),
            trainNo[trainNo.length - 3].digitToInt(),
            trainNo.takeLast(2),
        )
    }

    override fun getTrainRealTimes(stationId: Long): List<GetTrainRealTimesDto.TrainRealTime> {
        val station = stationLineReader.getById(stationId)
        val subwayLine = station.subwayLine

        val cachedData = redisClient.get("$TRAIN_REAL_TIME_REDIS_PREFIX${subwayLine.identity}-${stationId}")
        cachedData?.let {
            return objectMapper.readValue(
                cachedData,
                objectMapper.typeFactory.constructCollectionType(List::class.java, GetTrainRealTimesDto.TrainRealTime::class.java)
            )
        }

        var startIndex = 1
        var endIndex = 5
        var totalSize = startIndex
        val trainRealTimes = mutableListOf<GetTrainRealTimesDto.TrainRealTime>()
        while (startIndex <= totalSize) {
            val trainRealTimesPublicData = seoulTrainClient.getTrainRealTimes(station.name, startIndex, endIndex)
            totalSize = trainRealTimesPublicData.errorMessage?.total ?: break
            startIndex = endIndex + 1
            endIndex = startIndex + 4
            trainRealTimes.addAll(generateTrainRealTimeList(trainRealTimesPublicData))
        }

        if (trainRealTimes.size == 0) throw BusinessException(ResponseCode.NOT_EXIST_ARRIVAL_TRAIN)

        val trainRealTimeMap = trainRealTimes.groupBy { it.subwayId }
            .mapValues { (_, trainRealTimes) ->
                trainRealTimes.sortedWith(
                    compareBy<GetTrainRealTimesDto.TrainRealTime> { it.currentTrainArrivalCode.priority }
                        .thenBy { it.currentLocation }
                )
            }
        trainRealTimeMap.forEach { redisClient.set("$TRAIN_REAL_TIME_REDIS_PREFIX${it.key}-$stationId", it.value, TRAIN_REAL_TIME_REDIS_EXPIRE_SEC, TimeUnit.SECONDS)  }

        return trainRealTimeMap.getOrElse(subwayLine.identity.toString()) { emptyList() }
    }

    private fun generateTrainRealTimeList(trainRealTime: TrainRealTimeDto): List<GetTrainRealTimesDto.TrainRealTime> {
        return trainRealTime.realtimeArrivalList
            ?.map {
                val trainDirection = it.trainLineNm.split("-")
                GetTrainRealTimesDto.TrainRealTime(
                    subwayId = it.subwayId,
                    upDownType = UpDownType.from(it.updnLine),
                    nextStationDirection = trainDirection[1].trim(),
                    destinationStationDirection = trainDirection[0].trim(),
                    trainNum = it.btrainNo,
                    currentLocation = it.arvlMsg2.replace("\\d+초 후".toRegex(), "").trim(),
                    currentTrainArrivalCode = TrainArrivalCode.from(it.arvlCd)
                )
            }
            ?: emptyList()
    }
}