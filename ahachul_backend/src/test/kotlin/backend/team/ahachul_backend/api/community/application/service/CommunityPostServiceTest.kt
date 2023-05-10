package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.CreateCommunityPostCommand
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.DeleteCommunityPostCommand
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.GetCommunityPostCommand
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.UpdateCommunityPostCommand
import backend.team.ahachul_backend.api.community.adapter.web.out.CommunityPostRepository
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityPostUseCase
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.api.member.adapter.web.out.MemberRepository
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.MemberStatusType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.api.community.domain.model.CommunityPostType
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.model.RegionType
import backend.team.ahachul_backend.common.persistence.SubwayLineRepository
import backend.team.ahachul_backend.common.utils.RequestUtils
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class CommunityPostServiceTest(
    @Autowired val communityPostRepository: CommunityPostRepository,
    @Autowired val communityPostUseCase: CommunityPostUseCase,
    @Autowired val memberRepository: MemberRepository,
    @Autowired val subwayLineRepository: SubwayLineRepository,
) {

    var member: MemberEntity? = null
    private lateinit var subwayLine: SubwayLineEntity

    @BeforeEach
    fun setup() {
        member = memberRepository.save(
            MemberEntity(
            nickname = "nickname",
            provider = ProviderType.KAKAO,
            providerUserId = "providerUserId",
            email = "email",
            gender = GenderType.MALE,
            ageRange = "20",
            status = MemberStatusType.ACTIVE
            )
        )
        member!!.id.let { RequestUtils.setAttribute("memberId", it) }
        subwayLine = subwayLineRepository.save(SubwayLineEntity(name = "1호선", regionType = RegionType.METROPOLITAN))
    }

    @Test
    @DisplayName("커뮤니티 게시글 작성")
    fun createCommunityPost() {
        // given
        val command = CreateCommunityPostCommand(
            title = "제목",
            content = "내용",
            categoryType = CommunityCategoryType.FREE,
            subwayLineId = subwayLine.id
        )

        // when
        val result = communityPostUseCase.createCommunityPost(command)

        // then
        assertThat(result.id).isNotNull()
        assertThat(result.title).isEqualTo("제목")
        assertThat(result.content).isEqualTo("내용")
        assertThat(result.categoryType).isEqualTo(CommunityCategoryType.FREE)
        assertThat(result.region).isEqualTo(RegionType.METROPOLITAN)

        val communityPost = communityPostRepository.findById(result.id).get()

        assertThat(communityPost.member!!.id).isEqualTo(member!!.id)
    }

    @Test
    @DisplayName("커뮤니티 게시글 수정")
    fun updateCommunityPost() {
        // given
        val createCommand = CreateCommunityPostCommand(
            title = "제목",
            content = "내용",
            categoryType = CommunityCategoryType.FREE,
            subwayLineId = subwayLine.id
        )
        val (postId, _, _, _, _) = communityPostUseCase.createCommunityPost(createCommand)

        val updateCommand = UpdateCommunityPostCommand(
            id = postId,
            "수정된 제목",
            "수정된 내용",
            CommunityCategoryType.HUMOR
        )

        // when
        val result = communityPostUseCase.updateCommunityPost(updateCommand)

        // then
        assertThat(result.id).isEqualTo(postId)
        assertThat(result.title).isEqualTo("수정된 제목")
        assertThat(result.content).isEqualTo("수정된 내용")
        assertThat(result.categoryType).isEqualTo(CommunityCategoryType.HUMOR)
    }

    @Test
    @DisplayName("커뮤니티 게시글 수정 - 권한이 없는 경우")
    fun updateCommunityPostWithNotAuth() {
        // given
        val createCommand = CreateCommunityPostCommand(
            title = "제목",
            content = "내용",
            categoryType = CommunityCategoryType.FREE,
            subwayLineId = subwayLine.id
        )
        val (postId, _, _, _, _) = communityPostUseCase.createCommunityPost(createCommand)

        RequestUtils.setAttribute("memberId", 2)

        val updateCommand = UpdateCommunityPostCommand(
            id = postId,
            "수정된 제목",
            "수정된 내용",
            CommunityCategoryType.HUMOR
        )

        // when, then
        assertThatThrownBy {
            communityPostUseCase.updateCommunityPost(updateCommand)
        }
            .isExactlyInstanceOf(CommonException::class.java)
            .hasMessage("권한이 없습니다.")
    }

    @Test
    @DisplayName("커뮤니티 게시글 삭제")
    fun 커뮤니티_게시글_삭제() {
        // given
        val createCommand = CreateCommunityPostCommand(
            title = "제목",
            content = "내용",
            categoryType = CommunityCategoryType.FREE,
            subwayLineId = subwayLine.id
        )
        val (postId, _, _, _, _) = communityPostUseCase.createCommunityPost(createCommand)

        val deleteCommand = DeleteCommunityPostCommand(postId)

        // when, then
        val result = communityPostRepository.findById(postId).get()

        assertThat(result.status).isEqualTo(CommunityPostType.CREATED)
        communityPostUseCase.deleteCommunityPost(deleteCommand)
        assertThat(result.status).isEqualTo(CommunityPostType.DELETED)
    }

    @Test
    @DisplayName("커뮤니티 게시글 단 건 조회")
    fun 커뮤니티_게시글_단건_조회() {
        // given
        val createCommand = CreateCommunityPostCommand(
            title = "제목",
            content = "내용",
            categoryType = CommunityCategoryType.FREE,
            subwayLineId = subwayLine.id
        )
        val (postId, _, _, _, _) = communityPostUseCase.createCommunityPost(createCommand)

        val getCommunityPostCommand = GetCommunityPostCommand(
            id = postId
        )

        // when
        val result = communityPostUseCase.getCommunityPost(getCommunityPostCommand)

        // then
        assertThat(result.id).isEqualTo(postId)
        assertThat(result.title).isEqualTo(result.title)
        assertThat(result.content).isEqualTo(result.content)
        assertThat(result.categoryType).isEqualTo(CommunityCategoryType.FREE)
        assertThat(result.region).isEqualTo(RegionType.METROPOLITAN)
        assertThat(result.writer).isEqualTo(member?.nickname)
    }

//    @Test TODO
    @DisplayName("커뮤니티 조회수 증가")
    fun 커뮤니티_조회수_증가() {
        // given
        val createCommand = CreateCommunityPostCommand(
            title = "제목",
            content = "내용",
            categoryType = CommunityCategoryType.FREE,
            subwayLineId = subwayLine.id
        )
        val (postId, _, _, _, _) = communityPostUseCase.createCommunityPost(createCommand)

        val getCommunityPostCommand = GetCommunityPostCommand(
            id = postId
        )

        // when, then
        var result = communityPostUseCase.getCommunityPost(getCommunityPostCommand)
        assertThat(result.views).isEqualTo(1)

        result = communityPostUseCase.getCommunityPost(getCommunityPostCommand)
        assertThat(result.views).isEqualTo(2)

        communityPostUseCase.getCommunityPost(getCommunityPostCommand)
        result = communityPostUseCase.getCommunityPost(getCommunityPostCommand)
        assertThat(result.views).isEqualTo(4)
    }
}