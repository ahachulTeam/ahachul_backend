package backend.team.ahachul_backend.api.member.application.port.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetMemberDto

interface MemberUseCase {

    fun getMember(): GetMemberDto.Response
}