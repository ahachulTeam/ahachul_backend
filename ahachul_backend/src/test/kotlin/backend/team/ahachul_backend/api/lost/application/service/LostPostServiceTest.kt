package backend.team.ahachul_backend.api.lost.application.service

import backend.team.ahachul_backend.api.lost.adapter.web.out.LostPostRepository
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.api.member.adapter.web.out.MemberRepository
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.MemberStatus
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.assertj.core.api.Assertions.assertThat
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
    @Autowired val memberRepository: MemberRepository
){

    var member: MemberEntity? = null

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
                status = MemberStatus.ACTIVE
            )
        )
        member!!.id?.let { RequestUtils.setAttribute("memberId", it)}
    }

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
        val entity = lostPostRepository.findById(response.id).get()
        assertThat(entity.title).isEqualTo("지갑")
    }

    @Test
    @DisplayName("유실물 수정 테스트 - 권한이 있는 경우")
    fun updateLostPost() {
        // given
        val createCommand = CreateLostPostCommand(
            title = "지갑",
            content = "하늘색 지갑 잃어버렸어요",
            lostLine = "1호선",
            lostType = LostType.LOST,
            imgUrls = listOf("11", "22")
        )

        val entity = lostPostUseCase.createLostPost(createCommand)

        val updateCommand = UpdateLostPostCommand(
            title = null,
            content = "파란색 지갑 잃어버렸어요",
            lostLine = "2호선",
            imgUrls = null,
            status = LostStatus.COMPLETE
        )

        // when
        val response = lostPostUseCase.updateLostPost(entity.id, updateCommand)

        // then
        assertThat(response.id).isNotNull
        assertThat(response.content).isEqualTo("파란색 지갑 잃어버렸어요")
        assertThat(response.lostLine).isEqualTo("2호선")
        assertThat(response.status).isEqualTo(LostStatus.COMPLETE)
    }
}