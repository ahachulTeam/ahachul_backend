package backend.team.ahachul_backend.api.report.application.service

import backend.team.ahachul_backend.api.lost.adapter.web.out.LostPostRepository
import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.api.member.adapter.web.out.MemberRepository
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.MemberStatusType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.api.report.application.port.`in`.ReportUseCase
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.model.RegionType
import backend.team.ahachul_backend.common.persistence.SubwayLineRepository
import backend.team.ahachul_backend.common.utils.RequestUtils
import backend.team.ahachul_backend.config.controller.CommonServiceTestConfig
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class ReportServiceTest(
    @Autowired val reportUseCase: ReportUseCase,
    @Autowired val memberRepository: MemberRepository,
    @Autowired val lostPostRepository: LostPostRepository,
    @Autowired val subwayLineRepository: SubwayLineRepository
): CommonServiceTestConfig() {

    private var subwayLine: SubwayLineEntity? = null
    private var member: MemberEntity? = null
    private var otherMember: MemberEntity? = null

    @BeforeEach
    fun setup() {
        member = memberRepository.save(createMember("닉네임1"))
        member!!.id.let { RequestUtils.setAttribute("memberId", it) }
        otherMember = memberRepository.save(createMember("닉네임2"))
        subwayLine = createSubwayLine()
    }

    @Test
    @DisplayName("신고 저장")
    fun saveReport() {
        // given
        val target = lostPostRepository.save(createLostPost())

        // when
        val result = reportUseCase.saveReport(target.id, "lost")

        // then
        Assertions.assertThat(result.targetId).isEqualTo(target.id)
        Assertions.assertThat(result.sourceMemberId).isEqualTo(member!!.id)
        Assertions.assertThat(result.targetMemberId).isEqualTo(otherMember!!.id)
    }

    private fun createMember(nickname: String): MemberEntity {
        return MemberEntity(
            nickname = nickname,
            provider = ProviderType.KAKAO,
            providerUserId = "providerUserId",
            email = "email",
            gender = GenderType.MALE,
            ageRange = "20",
            status = MemberStatusType.ACTIVE
        )
    }

    private fun createSubwayLine(): SubwayLineEntity {
        return subwayLineRepository.save(
            SubwayLineEntity(
                name = "임시 호선",
                regionType = RegionType.METROPOLITAN
            )
        )
    }

    private fun createLostPost(): LostPostEntity {
        return LostPostEntity.of(
            command =  CreateLostPostCommand(
                title = "지갑",
                content = "내용",
                subwayLine = subwayLine!!.id,
                lostType = LostType.LOST,
                imgUrls = listOf("11", "22")
            ),
            member = otherMember!!,
            subwayLine = subwayLine!!
        )
    }
}