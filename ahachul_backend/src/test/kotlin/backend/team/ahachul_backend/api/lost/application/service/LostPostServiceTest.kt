package backend.team.ahachul_backend.api.lost.application.service

import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
@Transactional
class LostPostServiceTest(
    @Autowired val lostPostUseCase: LostPostUseCase
){

    @Test
    @DisplayName("유실물 저장 테스트")
    fun createLostPost() {
        // given
        val command = CreateLostPostCommand(
            title = "지갑",
            content = "하늘색 지갑 잃어버렸어요",
            lostLine = "1호선",
            lostType = LostType.LOST,
            imgUrls = listOf("11", "22")
        )

        // when
        val response = lostPostUseCase.createLostPost(command)

        // then
        assertThat(response.id).isNotNull
    }
}