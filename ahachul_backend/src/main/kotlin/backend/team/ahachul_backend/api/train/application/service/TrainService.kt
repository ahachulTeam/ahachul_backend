package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.api.train.application.port.out.TrainReader
import backend.team.ahachul_backend.api.train.domain.entity.TrainEntity
import backend.team.ahachul_backend.api.train.domain.model.TrainArrivalCode
import backend.team.ahachul_backend.api.train.domain.model.UpDownType
import backend.team.ahachul_backend.common.client.SeoulTrainClient
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
    private val subwayLineReader: SubwayLineReader,
    private val stationLineReader: SubwayLineReader,

    private val seoulTrainClient: SeoulTrainClient,
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

    override fun getTrainRealTimes(subwayLineId: Long, stationId: Long): GetTrainRealTimesDto.Response {
        val subwayLine = subwayLineReader.getById(subwayLineId)
        val station = stationLineReader.getById(stationId)
        var startIndex = 1
        var endIndex = 5
        var totalSize = startIndex
        val result = mutableListOf<GetTrainRealTimesDto.TrainRealTime>()
        while (startIndex <= totalSize) {
            val trainRealTimes = seoulTrainClient.getTrainRealTimes(station.name, startIndex, endIndex)
            totalSize = trainRealTimes.errorMessage?.total ?: -1
            startIndex = endIndex + 1
            endIndex = startIndex + 4
            result.addAll(extractCorrespondingSubwayLine(
                trainRealTime = trainRealTimes,
                subwayLineIdentity = subwayLine.identity,
            ))
        }
        val sortedResult = result.sortedWith(
            compareBy<GetTrainRealTimesDto.TrainRealTime> { it.currentTrainArrivalCode.code }
                .thenBy { it.currentLocation }
        )
        return GetTrainRealTimesDto.Response(subwayLineId, stationId, sortedResult)
    }

    private fun extractCorrespondingSubwayLine(trainRealTime: TrainRealTimeDto, subwayLineIdentity: Long): List<GetTrainRealTimesDto.TrainRealTime> {
        return trainRealTime.realtimeArrivalList
            ?.filter { it.subwayId == subwayLineIdentity.toString() }
            ?.map {
                val trainDirection = it.trainLineNm.split("-")
                GetTrainRealTimesDto.TrainRealTime(
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