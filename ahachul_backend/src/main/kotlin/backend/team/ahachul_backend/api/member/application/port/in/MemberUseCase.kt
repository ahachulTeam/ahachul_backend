package backend.team.ahachul_backend.api.member.application.port.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.CheckNicknameDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetMemberDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.UpdateMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.command.CheckNicknameCommand
import backend.team.ahachul_backend.api.member.application.port.`in`.command.UpdateMemberCommand

interface MemberUseCase {

    fun getMember(): GetMemberDto.Response

    fun updateMember(command: UpdateMemberCommand): UpdateMemberDto.Response

    fun checkNickname(command: CheckNicknameCommand): CheckNicknameDto.Response
}