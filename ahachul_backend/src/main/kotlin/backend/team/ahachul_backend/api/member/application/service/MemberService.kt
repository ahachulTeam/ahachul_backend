package backend.team.ahachul_backend.api.member.application.service

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetMemberDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.UpdateMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUseCase
import backend.team.ahachul_backend.api.member.application.port.`in`.command.UpdateMemberCommand
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
        private val memberReader: MemberReader
) : MemberUseCase {

    override fun getMember(): GetMemberDto.Response {
        val member = memberReader.getMember(RequestUtils.getAttribute("memberId").toLong())
        return GetMemberDto.Response.of(member)
    }

    @Transactional
    override fun updateMember(updateMemberCommand: UpdateMemberCommand): UpdateMemberDto.Response {
        val member = memberReader.getMember(RequestUtils.getAttribute("memberId").toLong())
        member.changeNickname(updateMemberCommand.nickname)
        member.changeGender(updateMemberCommand.gender)
        member.changeAgeRange(updateMemberCommand.ageRange)
        return UpdateMemberDto.Response.of(
                nickname = member.nickname!!,
                gender = member.gender!!,
                ageRange = member.ageRange!!
        )
    }
}

