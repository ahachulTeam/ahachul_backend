package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.member.adapter.web.out.MemberRepository
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.MemberStatusType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.api.train.domain.Congestion
import backend.team.ahachul_backend.common.client.TrainCongestionClient
import backend.team.ahachul_backend.common.client.dto.TrainCongestionDto
import backend.team.ahachul_backend.common.client.dto.TrainCongestionDto.Section
import backend.team.ahachul_backend.common.client.dto.TrainCongestionDto.Train
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.RequestUtils
import backend.team.ahachul_backend.config.controller.CommonServiceTestConfig
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean

class TrainCongestionServiceTest(
    @Autowired val trainCongestionService: TrainCongestionService,
    @Autowired val memberRepository: MemberRepository
): CommonServiceTestConfig() {

    @MockBean
    lateinit var trainCongestionClient: TrainCongestionClient

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
    fun 열차_혼잡도_퍼센트에_따라_색깔을_반환한다() {
        // given
        val subwayLine = 2
        val trainNo = 2034
        val congestionResult = TrainCongestionDto(
            success = true,
            code = 100,
            data = Train(
                subwayLine = "2",
                trainY = "2034",
                congestionResult = Section(
                    congestionTrain = "35",
                    congestionCar = "20|31|36|100|41|38|50|51|38|230",
                    congestionType =  1
                )
            )
        )
        given(trainCongestionClient.getCongestions(anyInt(), anyInt())).willReturn(congestionResult)

        // when
        val result = trainCongestionService.getTrainCongestion(subwayLine, trainNo)

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
            assertThat(result.congestions[i].congestionColor).isEqualTo(expected[i])
        }
    }

    @Test
    fun 지원하지_않는_노선이면_예외가_발생한다() {
        // given
        val subwayLine = 1
        val trainNo = 1034

        // when + then
        Assertions.assertThatThrownBy {
            trainCongestionService.getTrainCongestion(subwayLine, trainNo)
        }
            .isExactlyInstanceOf(BusinessException::class.java)
            .hasMessage(ResponseCode.INVALID_SUBWAY_LINE.message)
    }
}
