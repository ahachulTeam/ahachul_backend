package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment.CreateCommunityCommentCommand
import backend.team.ahachul_backend.api.community.adapter.web.out.CommunityCommentRepository
import backend.team.ahachul_backend.api.community.adapter.web.out.CommunityPostRepository
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityCommentUseCase
import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CommunityCommentServiceTest(
    @Autowired val communityCommentRepository: CommunityCommentRepository,
    @Autowired val communityCommentUseCase: CommunityCommentUseCase,
    @Autowired val communityPostRepository: CommunityPostRepository,
) {

    @Test
    @DisplayName("커뮤니티 코멘트 생성")
    fun 커뮤니티_코멘트_생성() {
        // given
        val post = communityPostRepository.save(
            CommunityPostEntity(
                title = "제목",
                content = "내용",
                categoryType = CommunityCategoryType.FREE,
            )
        )

        val createCommunityCommentCommand = CreateCommunityCommentCommand(
            postId = post.id,
            upperCommentId = null,
            content = "내용"
        )

        // when
        val result = communityCommentUseCase.createCommunityComment(createCommunityCommentCommand)

        // then
        assertThat(result.id).isNotNull()
        assertThat(result.content).isEqualTo("내용")
        assertThat(result.upperCommentId).isNull()

        val communityComment = communityCommentRepository.findById(result.id).get()

        assertThat(communityComment.id).isEqualTo(result.id)
    }
}