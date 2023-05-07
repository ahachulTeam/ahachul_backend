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
    var createCommand: CreateLostPostCommand? = null
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

        subwayLine = subwayLineRepository.save(
            SubwayLine(
                name = "1호선",
                regionType = RegionType.METROPOLITAN
            )
        )

        createCommand = CreateLostPostCommand(
            title = "지갑",
            content = "하늘색 지갑 잃어버렸어요",
            subwayLine = subwayLine!!.id,
            lostType = LostType.LOST,
            imgUrls = listOf("11", "22")
        )
    }

    @Test
    @DisplayName("유실물 상세 조회 테스트")
    fun getLostPost() {
        // given
        val entity = lostPostUseCase.createLostPost(createCommand!!)

        // when
        val response = lostPostUseCase.getLostPost(entity.id)

        // then
        assertThat(response.title).isEqualTo("지갑")
        assertThat(response.content).isEqualTo("하늘색 지갑 잃어버렸어요")
        assertThat(response.writer).isEqualTo("nickname")
        assertThat(response.subwayLine).isEqualTo(subwayLine!!.id)
        assertThat(response.status).isEqualTo(LostStatus.PROGRESS)
    }


    @Test
    @DisplayName("유실물 전체 조회 및 페이징 테스트 - 노선 필터링")
    fun searchLostPostsBySubwayLine() {
        // given
        val subwayLine1 = subwayLineRepository.save(
            SubwayLine(
                name = "1호선",
                regionType = RegionType.METROPOLITAN
            )
        )

        val subwayLine2 = subwayLineRepository.save(
            SubwayLine(
                name = "2호선",
                regionType = RegionType.METROPOLITAN
            )
        )

        for(i: Int in 1.. 5) {
            val createCommand1 = CreateLostPostCommand(
                title = "지갑",
                content = "유실물$i",
                subwayLine = subwayLine1.id,
                lostType = LostType.LOST,
                imgUrls = listOf("11", "22")
            )

            val createCommand2 = CreateLostPostCommand(
                title = "지갑",
                content = "유실물$i",
                subwayLine = subwayLine2.id,
                lostType = LostType.LOST,
                imgUrls = listOf("11", "22")
            )

            lostPostUseCase.createLostPost(createCommand1)
            lostPostUseCase.createLostPost(createCommand2)
        }

        val searchCommand1 = SearchLostPostCommand.of(
            PageRequest.of(0, 3),
            LostType.LOST,
            subwayLine1.id,
            null
        )

        val searchCommand2 = SearchLostPostCommand.of(
            PageRequest.of(1, 3),
            LostType.LOST,
            subwayLine1.id,
            null
        )

        // when + then
        val response1 = lostPostUseCase.searchLostPosts(searchCommand1)

        assertThat(response1.hasNext).isEqualTo(true)
        assertThat(response1.contents.size).isEqualTo(3)
        assertThat(response1.contents)
            .extracting("subwayLine")
            .usingRecursiveComparison()
            .isEqualTo((1.. 3).map {subwayLine1.id}.toList())
        assertThat(response1.contents)
            .extracting("content")
            .usingRecursiveComparison()
            .isEqualTo((5 downTo 3).map { "유실물$it" })

        // when + then
        val response2 = lostPostUseCase.searchLostPosts(searchCommand2)
        assertThat(response2.hasNext).isEqualTo(false)
        assertThat(response2.contents.size).isEqualTo(2)
        assertThat(response2.contents)
            .extracting("content")
            .usingRecursiveComparison()
            .isEqualTo((2 downTo 1).map { "유실물$it" })
    }

    @Test
    @DisplayName("유실물 전체 조회 테스트 - 유실물 / 습득물 필터링")
    fun searchLostPostsByLostType() {

    }
    @Test
    @DisplayName("유실물 저장 테스트")
    fun createLostPost() {
        // when
        val response = lostPostUseCase.createLostPost(createCommand!!)
        assertThat(response.id).isNotNull

        // then
        val entity = lostPostRepository.findById(response.id).get()

        assertThat(entity.title).isEqualTo("지갑")
        assertThat(entity.lostType).isEqualTo(LostType.LOST)
        assertThat(entity.type).isEqualTo(LostPostType.CREATED)
    }

    @Test
    @DisplayName("유실물 수정 테스트 - 권한이 있는 경우")
    fun updateLostPost() {
        // given
        val entity = lostPostUseCase.createLostPost(createCommand!!)

        val updateCommand = UpdateLostPostCommand(
            title = null,
            content = "파란색 지갑 잃어버렸어요",
            subwayLine = subwayLine!!.id,
            imgUrls = null,
            status = LostStatus.COMPLETE
        )

        // when
        val response = lostPostUseCase.updateLostPost(entity.id, updateCommand)

        // then
        assertThat(response.id).isNotNull
        assertThat(response.content).isEqualTo("파란색 지갑 잃어버렸어요")
        assertThat(response.subwayLine).isEqualTo(subwayLine!!.id)
        assertThat(response.status).isEqualTo(LostStatus.COMPLETE)
    }

    @Test
    @DisplayName("유실물 수정 테스트 - 권한이 없는 경우")
    fun updateLostPostUnAuthorized() {
        // given
        val entity = lostPostUseCase.createLostPost(createCommand!!)

        val updateCommand = UpdateLostPostCommand(
            title = null,
            content = "파란색 지갑 잃어버렸어요",
            subwayLine = null,
            imgUrls = null,
            status = LostStatus.COMPLETE
        )

        // when, then
        RequestUtils.setAttribute("memberId", 2)

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
        val entity = lostPostUseCase.createLostPost(createCommand!!)

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
        val entity = lostPostUseCase.createLostPost(createCommand!!)

        // when, then
        RequestUtils.setAttribute("memberId", 2)

        assertThatThrownBy {
            lostPostUseCase.deleteLostPost(entity.id)
        }
            .isExactlyInstanceOf(CommonException::class.java)
            .hasMessage(ResponseCode.INVALID_AUTH.message)
    }
}