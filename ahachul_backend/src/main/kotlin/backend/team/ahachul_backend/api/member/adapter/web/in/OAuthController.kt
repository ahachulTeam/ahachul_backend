package backend.team.ahachul_backend.api.member.adapter.web.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetRedirectUrlDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetTokenDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.LoginMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.OAuthUseCase
import backend.team.ahachul_backend.api.member.application.port.`in`.command.GetRedirectUrlCommand
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.response.CommonResponse
import backend.team.ahachul_backend.common.response.ResponseCode
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OAuthController(
        private val oAuthUseCase: OAuthUseCase,
) {

    @GetMapping("/v1/oauth/redirect-url")
    fun getRedirectUrl(@RequestParam providerType: ProviderType): CommonResponse<GetRedirectUrlDto.Response> {
        return CommonResponse.success(oAuthUseCase.getRedirectUrl(GetRedirectUrlCommand(providerType)))
    }

    @PostMapping("/v1/oauth/login")
    fun login(@RequestBody request: LoginMemberDto.Request): CommonResponse<LoginMemberDto.Response> {
        return CommonResponse.success(oAuthUseCase.login(request.toCommand()))
    }

    @PostMapping("/v1/oauth/token/refresh")
    fun getToken(@RequestBody request: GetTokenDto.Request): GetTokenDto.Response {
        try {
            return oAuthUseCase.getToken(request.toCommand())
        } catch (e: Exception) {
            throw when (e) {
                is SignatureException, is UnsupportedJwtException, is IllegalArgumentException, is MalformedJwtException -> CommonException(ResponseCode.INVALID_REFRESH_TOKEN)
                is ExpiredJwtException -> CommonException(ResponseCode.EXPIRED_REFRESH_TOKEN)
                else -> CommonException(ResponseCode.INTERNAL_SERVER_ERROR)
            }
        }
    }
}