package backend.team.ahachul_backend.api.lost.application.service

import backend.team.ahachul_backend.api.lost.adapter.web.out.LostPostRepository
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.api.lost.domain.model.LostPostType
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.member.adapter.web.out.MemberRepository
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.MemberStatusType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.common.persistence.SubwayLineRepository
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
    var subwayLineEntity: SubwayLineEntity? = null

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

        subwayLineEntity = subwayLineRepository.save(
            SubwayLineEntity(
                name = "1호선",
                regionType = RegionType.METROPOLITAN
            )
        )

        createCommand = CreateLostPostCommand(
            title = "지갑",
            content = "하늘색 지갑 잃어버렸어요",
            subwayLine = subwayLineEntity!!.id,
            lostType = LostType.LOST,
            imgUrls = listOf("11", "22")
        )
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
            id = entity.id,
            title = null,
            content = "파란색 지갑 잃어버렸어요",
            subwayLine = subwayLineEntity!!.id,
            imgUrls = null,
            status = LostStatus.COMPLETE
        )

        // when
        val response = lostPostUseCase.updateLostPost(updateCommand)

        // then
        assertThat(response.id).isNotNull
        assertThat(response.content).isEqualTo("파란색 지갑 잃어버렸어요")
        assertThat(response.subwayLine).isEqualTo(subwayLineEntity!!.id)
        assertThat(response.status).isEqualTo(LostStatus.COMPLETE)
    }

    @Test
    @DisplayName("유실물 수정 테스트 - 권한이 없는 경우")
    fun updateLostPostUnAuthorized() {
        // given
        val entity = lostPostUseCase.createLostPost(createCommand!!)

        val updateCommand = UpdateLostPostCommand(
            id = entity.id,
            title = null,
            content = "파란색 지갑 잃어버렸어요",
            subwayLine = null,
            imgUrls = null,
            status = LostStatus.COMPLETE
        )

        // when, then
        RequestUtils.setAttribute("memberId", 2)

        assertThatThrownBy {
            lostPostUseCase.updateLostPost(updateCommand)
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