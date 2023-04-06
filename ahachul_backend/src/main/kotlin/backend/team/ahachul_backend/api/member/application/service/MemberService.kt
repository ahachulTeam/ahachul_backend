package backend.team.ahachul_backend.api.member.application.service

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUseCase
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service

@Service
class MemberService(
        private val memberReader: MemberReader
) : MemberUseCase {

    override fun getMember(): GetMemberDto.Response {
        val member = memberReader.getMember(RequestUtils.getAttribute("memberId").toLong())
        return GetMemberDto.Response.of(member)
    }
}

