package backend.team.ahachul_backend.api.member.application.port.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.LoginMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.command.LoginMemberCommand

interface OAuthUseCase {

    fun login(command: LoginMemberCommand): LoginMemberDto.Response
}