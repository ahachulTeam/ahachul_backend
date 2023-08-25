package backend.team.ahachul_backend.api.rank.application.service

import backend.team.ahachul_backend.config.controller.CommonServiceTestConfig
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class HashTagRankServiceTest(
    @Autowired val hashTagRankService: HashTagRankService
): CommonServiceTestConfig() {

    @Test
    fun 해시태그_내림차순_랭킹() {
        for (i in 1..3) {
            hashTagRankService.increaseCount("1호선")
        }

        for (i in 1..5) {
            hashTagRankService.increaseCount("2호선")
        }

        val result = hashTagRankService.getRank()
        Assertions.assertThat(result.ranks.size).isEqualTo(2)
        Assertions.assertThat(result.ranks).isEqualTo(listOf("2호선", "1호선"))
    }
}
