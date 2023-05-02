package backend.team.ahachul_backend.api.member.application.service

import backend.team.ahachul_backend.api.member.adapter.web.out.MemberRepository
import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUseCase
import backend.team.ahachul_backend.api.member.application.port.`in`.command.CheckNicknameCommand
import backend.team.ahachul_backend.api.member.application.port.`in`.command.UpdateMemberCommand
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.MemberStatusType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MemberServiceTest(
        @Autowired val memberRepository: MemberRepository,
        @Autowired val memberUseCase: MemberUseCase
) {

    @BeforeEach
    fun setup() {
        val member = memberRepository.save(MemberEntity(
                nickname = "nickname",
                provider = ProviderType.KAKAO,
                providerUserId = "providerUserId",
                email = "email",
                gender = GenderType.MALE,
                ageRange = "20",
                status = MemberStatusType.ACTIVE
        ))
        member.id.let { RequestUtils.setAttribute("memberId", it) }
    }

    @Test
    @DisplayName("사용자 정보 조회")
    fun 사용자_정보_조회() {
        // when
        val result = memberUseCase.getMember()

        // then
        assertThat(result.memberId).isEqualTo(RequestUtils.getAttribute("memberId")!!.toLong())
        assertThat(result.email).isEqualTo("email")
        assertThat(result.gender).isEqualTo(GenderType.MALE)
        assertThat(result.ageRange).isEqualTo("20")
    }

    @Test
    @DisplayName("사용자 정보 수정")
    fun 사용자_정보_수정() {
        // given
        val command = UpdateMemberCommand(
                nickname = "afterNickname",
                gender = GenderType.FEMALE,
                ageRange = "30"
        )

        // when
        memberUseCase.updateMember(command)

        // then
        val result = memberUseCase.getMember()

        assertThat(result.nickname).isEqualTo("afterNickname")
        assertThat(result.gender).isEqualTo(GenderType.FEMALE)
        assertThat(result.ageRange).isEqualTo("30")
    }

    @Test
    @DisplayName("사용자 닉네임 사용 가능 여부 체크")
    fun 사용자_닉네임_사용가능_여부_체크() {
        // given
        val availableCommand = CheckNicknameCommand(
            nickname = "nickname2"
        )
        val unavailableCommand = CheckNicknameCommand(
            nickname = "nickname"
        )

        // when
        val availableResult = memberUseCase.checkNickname(availableCommand)
        val unavailableResult = memberUseCase.checkNickname(unavailableCommand)

        // then
        assertThat(availableResult.available).isEqualTo(true)
        assertThat(unavailableResult.available).isEqualTo(false)
    }
}