package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto
import backend.team.ahachul_backend.api.train.domain.model.TrainArrivalCode
import backend.team.ahachul_backend.api.train.domain.model.UpDownType
import backend.team.ahachul_backend.config.controller.CommonServiceTestConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class TrainCacheUtilsTest(
    @Autowired val trainCacheUtils: TrainCacheUtils
): CommonServiceTestConfig() {

//    @Test
//    fun 가장_역에_근접한_열차의_순으로_정렬한다() {
//        // given
//        val realTimeTrainData = listOf(
//            createTrainRealTime(1, "2236", "6분", TrainArrivalCode.RUNNING),
//            createTrainRealTime(2, "2238", "전역 도착", TrainArrivalCode.BEFORE_STATION_ARRIVE),
//            createTrainRealTime(1, "2234", "전역 도착", TrainArrivalCode.BEFORE_STATION_ARRIVE)
//        )
//
//        // when
//        val result = trainCacheUtils.getSortedData(realTimeTrainData)
//
//        // then
//        assertThat(result[0].trainNum).isEqualTo(realTimeTrainData[2].trainNum)
//        assertThat(result[1].trainNum).isEqualTo(realTimeTrainData[1].trainNum)
//        assertThat(result[2].trainNum).isEqualTo(realTimeTrainData[0].trainNum)
//    }

    private fun createTrainRealTime(
        stationOrder: Int, trainNum: String , currentLocation: String, currentTrainArrivalCode: TrainArrivalCode)
    : GetTrainRealTimesDto.TrainRealTime{
        return GetTrainRealTimesDto.TrainRealTime(
            subwayId = "",
            stationOrder = stationOrder,
            upDownType = UpDownType.DOWN,
            nextStationDirection = "신대방방면",
            destinationStationDirection = "성수행",
            trainNum = trainNum,
//            currentLocation = currentLocation,
            currentTrainArrivalCode = currentTrainArrivalCode
        )
    }
}
