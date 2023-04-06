package backend.team.ahachul_backend.api.member.adapter.web.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUseCase
import backend.team.ahachul_backend.common.annotation.Authentication
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
        private val memberUseCase: MemberUseCase
) {

    @Authentication
    @GetMapping("/v1/members")
    fun getMember(): CommonResponse<GetMemberDto.Response> {
        return CommonResponse.success(memberUseCase.getMember())
    }
}