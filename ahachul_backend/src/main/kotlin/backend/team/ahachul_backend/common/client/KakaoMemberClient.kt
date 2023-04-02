package backend.team.ahachul_backend.common.client

import backend.team.ahachul_backend.common.dto.KakaoUserInfoDto

interface KakaoMemberClient {

    fun getAccessTokenByCode(code: String): String

    fun getUserInfoByAccessToken(accessToken: String): KakaoUserInfoDto
}