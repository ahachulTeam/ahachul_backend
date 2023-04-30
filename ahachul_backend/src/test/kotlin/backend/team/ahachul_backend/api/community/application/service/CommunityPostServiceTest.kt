package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts.CreateCommunityPostCommand
import backend.team.ahachul_backend.api.community.adapter.web.out.CommunityPostRepository
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityPostUseCase
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.model.RegionType
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class CommunityPostServiceTest(
    @Autowired val customerPostRepository: CommunityPostRepository,
    @Autowired val customerUseCase: CommunityPostUseCase
) {

    @Test
    @DisplayName("커뮤니티 게시글 작성")
    fun 커뮤니티_게시글_작성() {
        // given
        val command = CreateCommunityPostCommand(
            title = "제목",
            content = "내용",
            categoryType = CommunityCategoryType.FREE
        )

        // when
        val result = customerUseCase.createCommunityPost(command)

        // then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.title).isEqualTo("제목")
        assertThat(result.content).isEqualTo("내용")
        assertThat(result.categoryType).isEqualTo(CommunityCategoryType.FREE)
        assertThat(result.region).isEqualTo(RegionType.METROPOLITAN)
    }
}