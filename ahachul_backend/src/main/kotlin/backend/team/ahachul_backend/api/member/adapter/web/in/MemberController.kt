package backend.team.ahachul_backend.api.member.adapter.web.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.LoginMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUserCase
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
        private val memberUserCase: MemberUserCase
) {

    @PostMapping("/v1/members/login")
    fun login(@RequestBody request: LoginMemberDto.Request): CommonResponse<LoginMemberDto.Response>? {
        return CommonResponse.success(memberUserCase.login(request.toCommand()))
    }
}