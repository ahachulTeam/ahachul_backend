package backend.team.ahachul_backend.api.member.application.service

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.LoginMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUserCase
import backend.team.ahachul_backend.api.member.application.port.`in`.command.LoginMemberCommand
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.common.client.KakaoMemberClient
import backend.team.ahachul_backend.common.dto.KakaoUserInfoDto
import backend.team.ahachul_backend.common.properties.JwtProperties
import backend.team.ahachul_backend.common.utils.JwtUtils
import org.springframework.stereotype.Service

@Service
class MemberService(
        private val kakaoMemberClient: KakaoMemberClient,
        private val jwtUtils: JwtUtils,
        private val jwtProperties: JwtProperties
): MemberUserCase {

    override fun login(command: LoginMemberCommand): LoginMemberDto.Response {
        if (command.providerType == ProviderType.KAKAO) {
            val userInfo = kakaoLogin(command.providerCode)
            LoginMemberDto.Response(
                    nickname = userInfo.nickname,
                    accessToken = jwtUtils.createToken(userInfo.sub, jwtProperties.accessTokenExpireTime.toLong()),
                    accessTokenExpiresIn = jwtProperties.accessTokenExpireTime,
                    refreshToken = jwtUtils.createToken(userInfo.sub, jwtProperties.refreshTokenExpireTime.toLong()),
                    refreshTokenExpiresIn = jwtProperties.refreshTokenExpireTime
            )
        }
        
        return LoginMemberDto.Response("", "", "", "", "")
    }

    private fun kakaoLogin(provideCode: String): KakaoUserInfoDto {
        val accessToken = kakaoMemberClient.getAccessTokenByCode(provideCode)
        return kakaoMemberClient.getUserInfoByAccessToken(accessToken)
    }
}