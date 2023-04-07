package backend.team.ahachul_backend.api.member.application.service

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetRedirectUrlDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetTokenDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.LoginMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.OAuthUseCase
import backend.team.ahachul_backend.api.member.application.port.`in`.command.GetRedirectUrlCommand
import backend.team.ahachul_backend.api.member.application.port.`in`.command.GetTokenCommand
import backend.team.ahachul_backend.api.member.application.port.`in`.command.LoginMemberCommand
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.api.member.application.port.out.MemberWriter
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.common.client.GoogleMemberClient
import backend.team.ahachul_backend.common.client.KakaoMemberClient
import backend.team.ahachul_backend.common.dto.GoogleUserInfoDto
import backend.team.ahachul_backend.common.dto.KakaoMemberInfoDto
import backend.team.ahachul_backend.common.properties.JwtProperties
import backend.team.ahachul_backend.common.properties.OAuthProperties
import backend.team.ahachul_backend.common.utils.JwtUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Service
@Transactional(readOnly=true)
class OAuthService(
        private val memberWriter: MemberWriter,
        private val memberReader: MemberReader,
        private val kakaoMemberClient: KakaoMemberClient,
        private val googleMemberClient: GoogleMemberClient,
        private val jwtUtils: JwtUtils,
        private val jwtProperties: JwtProperties,
        private val oAuthProperties: OAuthProperties,
): OAuthUseCase {

    companion object {
        const val sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000
    }

    @Transactional
    override fun login(command: LoginMemberCommand): LoginMemberDto.Response {
        val memberId = when (command.providerType) {
            ProviderType.KAKAO -> {
                val userInfo = getKakaoMemberInfo(command.providerCode)
                val member = memberReader.findMember(userInfo.id)
                member?.id?.toString() ?: memberWriter.save(MemberEntity.ofKakao(command, userInfo)).toString()

            }
            ProviderType.GOOGLE -> {
                val userInfo = getGoogleMemberInfo(command.providerCode)
                val member = memberReader.findMember(userInfo!!.id)
                member?.id?.toString() ?: memberWriter.save(MemberEntity.ofGoogle(command, userInfo)).toString()
            }
        }
        return makeLoginResponse(memberId)
    }

    private fun getKakaoMemberInfo(provideCode: String): KakaoMemberInfoDto {
        val accessToken = kakaoMemberClient.getAccessTokenByCode(provideCode)
        return kakaoMemberClient.getMemberInfoByAccessToken(accessToken)
    }

    private fun getGoogleMemberInfo(provideCode: String): GoogleUserInfoDto? {
        val accessToken = googleMemberClient.getAccessTokenByCode(provideCode)
        return googleMemberClient.getMemberInfoByAccessToken(accessToken!!)
    }

    private fun makeLoginResponse(memberId: String): LoginMemberDto.Response {
        return LoginMemberDto.Response(
                memberId = memberId,
                accessToken = jwtUtils.createToken(memberId, jwtProperties.accessTokenExpireTime),
                accessTokenExpiresIn = jwtProperties.accessTokenExpireTime,
                refreshToken = jwtUtils.createToken(memberId, jwtProperties.refreshTokenExpireTime),
                refreshTokenExpiresIn = jwtProperties.refreshTokenExpireTime
        )
    }

    override fun getToken(command: GetTokenCommand): GetTokenDto.Response {
        val refreshToken = jwtUtils.verify(command.refreshToken)

        if (refreshToken.body.expiration.after(Date(System.currentTimeMillis() - sevenDaysInMillis))) {
            return GetTokenDto.Response (
                    accessToken = jwtUtils.createToken(refreshToken.body.subject, jwtProperties.accessTokenExpireTime),
                    accessTokenExpiresIn = jwtProperties.accessTokenExpireTime,
                    refreshToken = jwtUtils.createToken(refreshToken.body.subject, jwtProperties.refreshTokenExpireTime),
                    refreshTokenExpiresIn = jwtProperties.refreshTokenExpireTime
            )
        }
        return GetTokenDto.Response (
            accessToken = jwtUtils.createToken(refreshToken.body.subject, jwtProperties.accessTokenExpireTime),
            accessTokenExpiresIn = jwtProperties.accessTokenExpireTime
        )
    }

    override fun getRedirectUrl(command: GetRedirectUrlCommand): GetRedirectUrlDto.Response {
        val providerTypeStr = command.providerType.toString().lowercase()
        val client = oAuthProperties.client[providerTypeStr]

        return GetRedirectUrlDto.Response(
            when (command.providerType) {
                ProviderType.KAKAO -> UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                        .queryParam("client_id", client!!.clientId)
                        .queryParam("redirect_uri", client.redirectUri)
                        .queryParam("response_type", "code")
                        .build()
                        .toString()
                ProviderType.GOOGLE -> UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                        .queryParam("client_id", client!!.clientId)
                        .queryParam("redirect_uri", client.redirectUri)
                        .queryParam("access_type", "offline")
                        .queryParam("response_type", "code")
                        .queryParam("scope", "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile")
                        .build()
                        .toString()
        })
    }
}

