package backend.team.ahachul_backend.api.lost.application.service

import backend.team.ahachul_backend.api.lost.adapter.web.out.LostPostRepository
import backend.team.ahachul_backend.api.lost.adapter.web.out.SubwayLineRepository
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.SearchLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.entity.SubwayLine
import backend.team.ahachul_backend.api.lost.domain.model.LostPostType
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.api.member.adapter.web.out.MemberRepository
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.MemberStatusType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.model.RegionType
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
@Transactional
class LostPostServiceTest(
    @Autowired val lostPostUseCase: LostPostUseCase,
    @Autowired val lostPostRepository: LostPostRepository,
    @Autowired val memberRepository: MemberRepository,
    @Autowired val subwayLineRepository: SubwayLineRepository
){

    var member: MemberEntity? = null
    var subwayLine: SubwayLine? = null

    @BeforeEach
    fun setUp() {
        member = memberRepository.save(
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
        member!!.id.let { RequestUtils.setAttribute("memberId", it)}

        subwayLine = createSubwayLine("1호선")
    }

    @Test
    @DisplayName("유실물 상세 조회 테스트")
    fun getLostPost() {
        // given
        val createCommand = createLostPostCommand(subwayLine!!.id, "내용")
        val entity = lostPostUseCase.createLostPost(createCommand)

        // when
        val response = lostPostUseCase.getLostPost(entity.id)

        // then
        assertThat(response.title).isEqualTo("지갑")
        assertThat(response.content).isEqualTo("내용")
        assertThat(response.writer).isEqualTo("nickname")
        assertThat(response.subwayLine).isEqualTo(subwayLine!!.id)
        assertThat(response.status).isEqualTo(LostStatus.PROGRESS)
    }

    @Test
    @DisplayName("유실물 전체 조회 페이징 테스트 - 필터링 X")
    fun searchLostPostPaging() {
        // given
        for(i: Int in 1.. 5) {
            val createCommand = createLostPostCommand(subwayLine!!.id, "유실물$i")
            lostPostUseCase.createLostPost(createCommand)
        }

        val searchCommand1 = createSearchLostPostCommand(0, subwayLine!!)
        val searchCommand2 = createSearchLostPostCommand(1, subwayLine!!)

        // when
        val response1 = lostPostUseCase.searchLostPosts(searchCommand1)
        val response2 = lostPostUseCase.searchLostPosts(searchCommand2)

        // then
        assertThat(response1.hasNext).isEqualTo(true)
        assertThat(response1.posts.size).isEqualTo(3)
        assertThat(response1.posts)
            .extracting("content")
            .usingRecursiveComparison()
            .isEqualTo((5 downTo 3).map { "유실물$it" })

        assertThat(response2.hasNext).isEqualTo(false)
        assertThat(response2.posts.size).isEqualTo(2)
        assertThat(response2.posts)
            .extracting("content")
            .usingRecursiveComparison()
            .isEqualTo((2 downTo 1).map { "유실물$it" })
    }

    @Test
    @DisplayName("유실물 전체 조회 테스트 - 노선 필터링")
    fun searchLostPostsBySubwayLine() {
        // given
        val subwayLine1 = createSubwayLine("1호선")
        val subwayLine2 = createSubwayLine("2호선")

        for(i: Int in 1.. 5) {
            val createCommand1 = createLostPostCommand(subwayLine1.id, "유실물$i")
            val createCommand2 = createLostPostCommand(subwayLine2.id, "유실물$i")

            lostPostUseCase.createLostPost(createCommand1)
            lostPostUseCase.createLostPost(createCommand2)
        }

        val searchCommand = createSearchLostPostCommand(0, subwayLine1)

        // when
        val response = lostPostUseCase.searchLostPosts(searchCommand)

        // then
        assertThat(response.posts.size).isEqualTo(3)
        assertThat(response.posts)
            .extracting("subwayLine")
            .usingRecursiveComparison()
            .isEqualTo((1.. 3).map {subwayLine1.id}.toList())
    }

    @Test
    @DisplayName("유실물 저장 테스트")
    fun createLostPost() {
        //given
        val createCommand = createLostPostCommand(subwayLine!!.id, "내용")

        // when
        val response = lostPostUseCase.createLostPost(createCommand)
        assertThat(response.id).isNotNull

        // then
        val entity = lostPostRepository.findById(response.id).get()

        assertThat(entity.title).isEqualTo("지갑")
        assertThat(entity.content).isEqualTo("내용")
        assertThat(entity.lostType).isEqualTo(LostType.LOST)
        assertThat(entity.type).isEqualTo(LostPostType.CREATED)
    }

    @Test
    @DisplayName("유실물 수정 테스트 - 권한이 있는 경우")
    fun updateLostPost() {
        // given
        val createCommand = createLostPostCommand(subwayLine!!.id, "내용")
        val entity = lostPostUseCase.createLostPost(createCommand)

        val updateCommand = UpdateLostPostCommand(
            title = null,
            content = "수정한 내용",
            subwayLine = subwayLine!!.id,
            imgUrls = null,
            status = LostStatus.COMPLETE
        )

        // when
        val response = lostPostUseCase.updateLostPost(entity.id, updateCommand)

        // then
        assertThat(response.id).isNotNull
        assertThat(response.content).isEqualTo("수정한 내용")
        assertThat(response.subwayLine).isEqualTo(subwayLine!!.id)
        assertThat(response.status).isEqualTo(LostStatus.COMPLETE)
    }

    @Test
    @DisplayName("유실물 수정 테스트 - 권한이 없는 경우")
    fun updateLostPostUnAuthorized() {
        // given
        val createCommand = createLostPostCommand(subwayLine!!.id, "내용")
        val entity = lostPostUseCase.createLostPost(createCommand)

        val updateCommand = UpdateLostPostCommand(
            title = null,
            content = "수정한 내용",
            subwayLine = null,
            imgUrls = null,
            status = LostStatus.COMPLETE
        )

        // when, then
        RequestUtils.setAttribute("memberId", member!!.id + 1)

        assertThatThrownBy {
            lostPostUseCase.updateLostPost(entity.id, updateCommand)
        }
            .isExactlyInstanceOf(CommonException::class.java)
            .hasMessage(ResponseCode.INVALID_AUTH.message)
    }

    @Test
    @DisplayName("유실물 삭제 테스트 - 권한이 있는 경우")
    fun deleteLostPost() {
        // given
        val createCommand = createLostPostCommand(subwayLine!!.id, "내용")
        val entity = lostPostUseCase.createLostPost(createCommand)

        // when
        val response = lostPostUseCase.deleteLostPost(entity.id)

        // then
        assertThat(response.id).isEqualTo(entity.id)
        assertThat(response.type).isEqualTo(LostPostType.DELETED)
    }

    @Test
    @DisplayName("유실물 삭제 테스트 - 권한이 없는 경우")
    fun deleteLostPostUnAuthorized() {
        // given
        val createCommand = createLostPostCommand(subwayLine!!.id, "내용")
        val entity = lostPostUseCase.createLostPost(createCommand)

        // when, then
        RequestUtils.setAttribute("memberId", member!!.id + 1)

        assertThatThrownBy {
            lostPostUseCase.deleteLostPost(entity.id)
        }
            .isExactlyInstanceOf(CommonException::class.java)
            .hasMessage(ResponseCode.INVALID_AUTH.message)
    }

    private fun createLostPostCommand(subwayLineId: Long, content: String): CreateLostPostCommand {
        return CreateLostPostCommand(
            title = "지갑",
            content = content,
            subwayLine = subwayLineId,
            lostType = LostType.LOST,
            imgUrls = listOf("11", "22")
        )
    }

    private fun createSubwayLine(name: String): SubwayLine {
        return subwayLineRepository.save(
            SubwayLine(
                name = name,
                regionType = RegionType.METROPOLITAN
            )
        )
    }

    private fun createSearchLostPostCommand(page: Int, subwayLine: SubwayLine): SearchLostPostCommand {
        return SearchLostPostCommand.of(
            PageRequest.of(page, 3),
            LostType.LOST,
            subwayLine.id,
            null
        )
    }
}