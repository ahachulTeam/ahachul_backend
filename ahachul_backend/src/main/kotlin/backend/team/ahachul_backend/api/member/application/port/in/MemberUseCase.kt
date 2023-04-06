package backend.team.ahachul_backend.api.member.application.port.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetMemberDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.UpdateMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.command.UpdateMemberCommand

interface MemberUseCase {

    fun getMember(): GetMemberDto.Response

    fun updateMember(updateMemberCommand: UpdateMemberCommand): UpdateMemberDto.Response
}