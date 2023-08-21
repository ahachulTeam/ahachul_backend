package backend.team.ahachul_backend.api.train.application.service

import backend.team.ahachul_backend.api.train.adapter.out.TrainRepository
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.api.train.domain.entity.TrainEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.model.RegionType
import backend.team.ahachul_backend.common.persistence.SubwayLineRepository
import backend.team.ahachul_backend.config.controller.CommonServiceTestConfig
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class TrainServiceTest(
    @Autowired val trainUseCase: TrainUseCase,
    @Autowired val subwayLineRepository: SubwayLineRepository,
    @Autowired val trainRepository: TrainRepository,
): CommonServiceTestConfig() {

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
}