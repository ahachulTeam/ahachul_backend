package backend.team.ahachul_backend.api.member.adapter.web.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.LoginMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.OAuthUseCase
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OAuthController(
        private val OAuthUseCase: OAuthUseCase
) {

    @GetMapping("/v1/oauth/{provider}/redirect-url")
    fun getRedirectUrl(): CommonResponse<String> {
        return CommonResponse.success("")
    }

    @PostMapping("/v1/oauth/login")
    fun login(@RequestBody request: LoginMemberDto.Request): CommonResponse<LoginMemberDto.Response> {
        return CommonResponse.success(OAuthUseCase.login(request.toCommand()))
    }
}