package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.common.application.port.out.StationReader
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetCongestionDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.api.train.application.port.`in`.command.GetCongestionCommand
import backend.team.ahachul_backend.api.train.application.port.out.TrainReader
import backend.team.ahachul_backend.api.train.domain.entity.TrainEntity
import backend.team.ahachul_backend.api.train.domain.model.TrainArrivalCode
import backend.team.ahachul_backend.api.train.domain.model.UpDownType
import backend.team.ahachul_backend.common.client.SeoulTrainClient
import backend.team.ahachul_backend.common.client.TrainCongestionClient
import backend.team.ahachul_backend.common.client.dto.TrainCongestionDto
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.dto.TrainRealTimeDto
import backend.team.ahachul_backend.common.exception.AdapterException
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.persistence.SubwayLineReader
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TrainService(
    private val trainReader: TrainReader,
    private val stationLineReader: StationReader,
    private val subwayLineReader: SubwayLineReader,

    private val seoulTrainClient: SeoulTrainClient,

    private val trainCacheUtils: TrainCacheUtils,
    private val trainCongestionClient: TrainCongestionClient,
    private val stationReader: StationReader,
): TrainUseCase {

    private val logger: Logger = Logger(javaClass)

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

    override fun getTrainRealTimes(stationId: Long, subwayLineId: Long): List<GetTrainRealTimesDto.TrainRealTime> {
        val station = stationLineReader.getById(stationId)
        val subwayLine = subwayLineReader.getById(subwayLineId)
        val subwayLineIdentity = subwayLine.identity

        val cachedData = trainCacheUtils.getCache(subwayLineIdentity, stationId)
        cachedData?.let {
            return cachedData
        }

        val trainRealTimeMap = requestTrainRealTimesAndSorting(station.name)
        trainRealTimeMap.forEach {
            trainCacheUtils.setCache(it.key.toLong(), stationId, it.value)
        }
        return trainRealTimeMap.getOrElse(subwayLineIdentity.toString()) { emptyList() }
    }

    private fun requestTrainRealTimesAndSorting(stationName: String): Map<String, List<GetTrainRealTimesDto.TrainRealTime>> {
        var startIndex = 1
        var endIndex = 5
        var totalSize = startIndex
        val trainRealTimes = mutableListOf<GetTrainRealTimesDto.TrainRealTime>()

        while (startIndex <= totalSize) {
            val trainRealTimesPublicData = seoulTrainClient.getTrainRealTimes(stationName, startIndex, endIndex)
            totalSize = trainRealTimesPublicData.errorMessage?.total ?: break
            startIndex = endIndex + 1
            endIndex = startIndex + 4
            trainRealTimes.addAll(generateTrainRealTimeList(trainRealTimesPublicData))
        }

        if (trainRealTimes.size == 0) {
            throw BusinessException(ResponseCode.NOT_EXIST_ARRIVAL_TRAIN)
        }

        return trainRealTimes
            .groupBy { it.subwayId!! }
            .mapValues {
                trainCacheUtils.getSortedData( it.value )
            }
    }

    private fun generateTrainRealTimeList(trainRealTime: TrainRealTimeDto): List<GetTrainRealTimesDto.TrainRealTime> {
        return trainRealTime.realtimeArrivalList
            ?.map {
                val trainDirection = it.trainLineNm.split("-")
                GetTrainRealTimesDto.TrainRealTime(
                    subwayId = it.subwayId,
                    stationOrder = extractStationOrder(it.arvlMsg2),
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

    private fun extractStationOrder(destinationMessage: String): Int {
        return if (destinationMessage.startsWith("[")) {
            pattern.find(destinationMessage)?.groupValues
                ?.getOrNull(1)
                ?.toInt()
                ?: Int.MAX_VALUE
        } else {
            Int.MAX_VALUE
        }
    }

    override fun getTrainCongestion(command: GetCongestionCommand): GetCongestionDto.Response {
        val station = stationReader.getById(command.stationId)
        val subwayLine = subwayLineReader.getById(command.subwayLineId)

        if (isInValidSubwayLine(subwayLine.id)) {
            throw BusinessException(ResponseCode.INVALID_SUBWAY_LINE)
        }

        val trains = getCachedTrainOrRequestExternal(subwayLine.id, subwayLine.identity, station.id)
        if (trains.isEmpty()) {
            return GetCongestionDto.Response.from(-1, emptyList())
        }

        val latestTrainNo = getLatestTrainNo(trains, subwayLine, command.upDownType)
        val response = trainCongestionClient.getCongestions(subwayLine.id, latestTrainNo.toInt())
        val trainCongestion = response.data!!
        val congestions = mapCongestionDto(response.success, trainCongestion)
        return GetCongestionDto.Response.from(trainCongestion.trainY.toInt(), congestions)
    }

    private fun isInValidSubwayLine(subwayLine: Long): Boolean {
        return !ALLOWED_SUBWAY_LINE.contains(subwayLine)
    }

    private fun getCachedTrainOrRequestExternal(
        subwayLineId: Long, subwayLineIdentity: Long, stationId: Long
    ): List<GetTrainRealTimesDto.TrainRealTime> {
        return trainCacheUtils.getCache(subwayLineIdentity, stationId)
                ?: getTrainRealTimes(stationId, subwayLineId)
    }

    private fun getLatestTrainNo(
        trains: List<GetTrainRealTimesDto.TrainRealTime>,
        subwayLine: SubwayLineEntity,
        upDownType: UpDownType
    ): String {
        val latestTrainNo = trains.first { it.upDownType == upDownType }.trainNum
        return when (subwayLine.isCorrectTrainNo(latestTrainNo)) {
            false -> "${subwayLine.id}${latestTrainNo.substring(1, latestTrainNo.length)}"
            true -> latestTrainNo
        }
    }

    private fun mapCongestionDto(
        success: Boolean, trainCongestion: TrainCongestionDto.Train
    ): List<GetCongestionDto.Section>  {
        if (success) {
            val congestionList = parse(trainCongestion.congestionResult.congestionCar)
            return congestionList.mapIndexed {
                    idx, it -> GetCongestionDto.Section.from(idx, it)
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
        val pattern = "\\[(\\d+)]".toRegex()
    }
}
