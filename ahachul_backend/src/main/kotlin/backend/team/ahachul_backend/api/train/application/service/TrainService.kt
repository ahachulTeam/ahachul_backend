package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.common.application.port.out.StationReader
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetCongestionDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.api.train.application.port.`in`.command.GetCongestionCommand
import backend.team.ahachul_backend.api.train.application.port.out.TrainReader
import backend.team.ahachul_backend.api.train.domain.entity.TrainEntity
import backend.team.ahachul_backend.common.client.SeoulTrainClient
import backend.team.ahachul_backend.common.client.TrainCongestionClient
import backend.team.ahachul_backend.common.client.dto.TrainCongestionDto
import backend.team.ahachul_backend.common.dto.RealtimeArrivalListDTO
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
    private val congestionCacheUtils: CongestionCacheUtils,
    private val trainCongestionClient: TrainCongestionClient,
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

        trainCacheUtils.getCache(subwayLineIdentity, stationId)?.let { return it }

        val trainRealTimeMap = requestTrainRealTimesAndSorting(station.name)
        trainRealTimeMap.forEach {
            trainCacheUtils.setCache(it.key.toLong(), stationId, it.value)
        }

        return trainRealTimeMap.getOrElse(subwayLineIdentity.toString()) { emptyList() }
    }

    private fun requestTrainRealTimesAndSorting(
        stationName: String
    ): Map<String, List<GetTrainRealTimesDto.TrainRealTime>> {
        var startIndex = 1
        var endIndex = 5
        var totalSize = startIndex
        val totalTrainRealTimes = mutableListOf<RealtimeArrivalListDTO>()

        while (startIndex <= totalSize) {
            val trainRealTimesPublicData = seoulTrainClient.getTrainRealTimes(stationName, startIndex, endIndex)
            totalSize = trainRealTimesPublicData.errorMessage?.total ?: break
            trainRealTimesPublicData.realtimeArrivalList?.let { totalTrainRealTimes.addAll(it) }
            startIndex = endIndex + 1
            endIndex = startIndex + 4
        }

        if (totalTrainRealTimes.isEmpty()) {
            throw BusinessException(ResponseCode.NOT_EXIST_ARRIVAL_TRAIN)
        }

        return totalTrainRealTimes
            .groupBy { it.subwayId }
            .mapValues { generateTrainRealTimeByUpDnType(it.value) }
    }

    private fun generateTrainRealTimeByUpDnType(trainRealTime: List<RealtimeArrivalListDTO>?): List<GetTrainRealTimesDto.TrainRealTime> {
        val total = mutableListOf<GetTrainRealTimesDto.TrainRealTime>()
        trainRealTime
            ?.groupBy { it.updnLine }
            ?.entries?.forEach { map ->
                val lis = map.value.map { dto ->
                    GetTrainRealTimesDto.TrainRealTime.from(dto, extractStationOrder(dto.arvlMsg2)) }
                    .sortedWith( compareBy(
                            { it.currentTrainArrivalCode.priority },
                            { it.stationOrder }
                    )).subList(0, 2)
                total.addAll(lis)
            }
        return total
    }

    /**
     * 도착 우선순위 추출
     */
    private fun extractStationOrder(destinationMessage: String): Int {
        return if (destinationMessage.startsWith("[")) {
            pattern.find(destinationMessage)!!.value.toInt().times(2)
        } else if (destinationMessage.contains("분")) {
            pattern.find(destinationMessage)!!.value.toInt()
        } else {
            Int.MAX_VALUE
        }
    }

    override fun getTrainCongestion(command: GetCongestionCommand): GetCongestionDto.Response {
        val subwayLineId = subwayLineReader.getById(command.subwayLineId).id
        val trainNo = command.trainNo

        congestionCacheUtils.getCache(subwayLineId, trainNo)?.let { return it }

        val correctTrainNum = getCorrectTrainNum(subwayLineId, trainNo)
        val response = trainCongestionClient.getCongestions(subwayLineId, correctTrainNum.toInt())
        val trainCongestion = response.data!!

        val congestions = mapCongestionDto(response.success, trainCongestion)
        val congestionDto = GetCongestionDto.Response.from(correctTrainNum, congestions)
        congestionCacheUtils.setCache(subwayLineId, correctTrainNum, congestionDto)
        return congestionDto
    }

    /**
     * API 자체에서 발생하는 열차 번호 에러 수정
     */
    private fun getCorrectTrainNum(subwayLineId: Long, trainNo: String): String {
        return when (trainNo[0] != subwayLineId.toString()[0]) {
            false -> "${subwayLineId}${trainNo.substring(1, trainNo.length)}"
            true -> trainNo
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
        const val DELIMITER = "|"
        val pattern = "[\\d+]".toRegex()
//        val pattern = "\\[(\\d+)]".toRegex()
    }
}
