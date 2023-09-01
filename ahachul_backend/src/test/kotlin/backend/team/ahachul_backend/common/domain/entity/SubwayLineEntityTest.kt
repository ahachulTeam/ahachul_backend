package backend.team.ahachul_backend.common.domain.entity

import backend.team.ahachul_backend.common.model.RegionType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SubwayLineEntityTest {

    @Test
    fun 잘못된_열차번호를_호선에_맞춰_가공한다() {
        // given
        val errorTrainNo = "8033"
        val subwayLine = SubwayLineEntity(
            id = 2,
            name = "2호선",
            regionType = RegionType.METROPOLITAN
        )

        // when
        val result = subwayLine.processCorrectTrainNo(errorTrainNo)

        // then
        assertThat(result).isEqualTo("2033")
    }
}
