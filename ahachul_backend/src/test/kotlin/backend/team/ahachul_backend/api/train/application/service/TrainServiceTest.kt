package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.common.application.port.out.StationReader
import backend.team.ahachul_backend.api.common.domain.entity.StationEntity
import backend.team.ahachul_backend.api.member.adapter.web.out.MemberRepository
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.MemberStatusType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetCongestionDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainRealTimesDto
import backend.team.ahachul_backend.api.train.adapter.out.TrainRepository
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.api.train.domain.model.Congestion
import backend.team.ahachul_backend.api.train.domain.entity.TrainEntity
import backend.team.ahachul_backend.api.train.domain.model.TrainArrivalCode
import backend.team.ahachul_backend.api.train.domain.model.UpDownType
import backend.team.ahachul_backend.common.client.TrainCongestionClient
import backend.team.ahachul_backend.common.client.dto.TrainCongestionDto
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.model.RegionType
import backend.team.ahachul_backend.common.persistence.SubwayLineRepository
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.RequestUtils
import backend.team.ahachul_backend.config.controller.CommonServiceTestConfig
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean

class TrainServiceTest(
    @Autowired val trainUseCase: TrainUseCase,
    @Autowired val subwayLineRepository: SubwayLineRepository,
    @Autowired val trainRepository: TrainRepository,
    @Autowired val memberRepository: MemberRepository
): CommonServiceTestConfig() {

    @MockBean
    lateinit var trainCongestionClient: TrainCongestionClient

    @MockBean
    lateinit var stationReader: StationReader

    @MockBean
    lateinit var trainCacheUtils: TrainCacheUtils

    lateinit var station: StationEntity

    @BeforeEach
    fun setUp() {
        val member = memberRepository.save(
            MemberEntity(
                nickname = "nickname",
                provider = ProviderType.GOOGLE,
                providerUserId = "providerUserId",
                email = "email",
                gender = GenderType.MALE,
                ageRange = "20",
                status = MemberStatusType.ACTIVE
            )
        )
        member.id.let { RequestUtils.setAttribute("memberId", it) }
    }

    @Test
    @DisplayName("열차번호 메타데이터 획득")
    fun getTrainTest() {
        // given
        val subwayLine = subwayLineRepository.save(
            SubwayLineEntity(
                name = "1호선",
                regionType = RegionType.METROPOLITAN
            )
        )
        val train = trainRepository.save(
            TrainEntity(
                prefixTrainNo = "0",
                subwayLine = subwayLine,
            )
        )

        // when
        val result = trainUseCase.getTrain("0342")

        // then
        assertThat(result.id).isEqualTo(train.id)
        assertThat(result.subwayLine.id).isEqualTo(subwayLine.id)
        assertThat(result.subwayLine.name).isEqualTo(subwayLine.name)
        assertThat(result.location).isEqualTo(3)
        assertThat(result.organizationTrainNo).isEqualTo("42")
    }

    @Test
    @DisplayName("열차번호 메타데이터 획득 실패")
    fun failGetTrainTest() {
        // when
        assertThatThrownBy {
            trainUseCase.getTrain("02342")
        }
            .isExactlyInstanceOf(BusinessException::class.java)
            .hasMessage("유효하지 않은 열차 번호입니다.")
    }

    @Test
    fun 열차_혼잡도_퍼센트에_따라_색깔을_반환한다() {
        // given
        val congestionResult = TrainCongestionDto(
            success = true,
            code = 100,
            data = TrainCongestionDto.Train(
                subwayLine = "2",
                trainY = "2034",
                congestionResult = TrainCongestionDto.Section(
                    congestionTrain = "35",
                    congestionCar = "20|31|36|100|41|38|50|51|38|230",
                    congestionType =  1
                )
            )
        )
        given(trainCongestionClient.getCongestions(ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt())).willReturn(congestionResult)

        val realTimeTrainData = listOf(
            createTrainRealTime(1, "2236", "6분", TrainArrivalCode.RUNNING),
            createTrainRealTime(2, "2238", "전역 도착", TrainArrivalCode.BEFORE_STATION_ARRIVE),
            createTrainRealTime(1, "2234", "전역 도착", TrainArrivalCode.BEFORE_STATION_ARRIVE)
        )
        given(trainCacheUtils.getCache(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).willReturn(realTimeTrainData)

        val subwayLine = SubwayLineEntity(
            id = 2,
            name = "2호선",
            regionType = RegionType.METROPOLITAN
        )

        station = StationEntity(
            id = 1,
            name = "뚝섬역",
            identity = 1002000210,
            subwayLine = subwayLine
        )

        given(stationReader.getById(ArgumentMatchers.anyLong())).willReturn(station)

        // when
        val result = trainUseCase.getTrainCongestion(
            GetCongestionDto.Request(
                stationId = station.id,
                upDownType = UpDownType.DOWN
            )
        )

        // then
        val expected = listOf(
            Congestion.SMOOTH.name,
            Congestion.SMOOTH.name,
            Congestion.MODERATE.name,
            Congestion.CONGESTED.name,
            Congestion.MODERATE.name,
            Congestion.MODERATE.name,
            Congestion.MODERATE.name,
            Congestion.MODERATE.name,
            Congestion.MODERATE.name,
            Congestion.VERY_CONGESTED.name
        )
        assertThat(result.congestions.size).isEqualTo(10)
        for (i: Int in 0 ..9) {
            assertThat(result.congestions[i].sectionNo).isEqualTo(i+1)
            assertThat(result.congestions[i].congestionColor).isEqualTo(expected[i])
        }
    }

    @Test
    fun 지원하지_않는_노선이면_예외가_발생한다() {
        // given
        val subwayLine = SubwayLineEntity(
            id = 1,
            name = "1호선",
            regionType = RegionType.METROPOLITAN
        )

        station = StationEntity(
            id = 1,
            name = "서울역",
            identity = 1002000210,
            subwayLine = subwayLine
        )

        given(stationReader.getById(ArgumentMatchers.anyLong())).willReturn(station)

        // when + then
        assertThatThrownBy {
            trainUseCase.getTrainCongestion(
                GetCongestionDto.Request(
                    stationId = station.id,
                    upDownType = UpDownType.DOWN
                )
            )
        }
            .isExactlyInstanceOf(BusinessException::class.java)
            .hasMessage(ResponseCode.INVALID_SUBWAY_LINE.message)
    }

    private fun createTrainRealTime(
        stationOrder: Int, trainNum: String , currentLocation: String, currentTrainArrivalCode: TrainArrivalCode
    )
            : GetTrainRealTimesDto.TrainRealTime{
        return GetTrainRealTimesDto.TrainRealTime(
            subwayId = "",
            stationOrder = stationOrder,
            upDownType = UpDownType.DOWN,
            nextStationDirection = "신대방방면",
            destinationStationDirection = "성수행",
            trainNum = trainNum,
            currentLocation = currentLocation,
            currentTrainArrivalCode = currentTrainArrivalCode
        )
    }
}
