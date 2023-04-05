package backend.team.ahachul_backend.common.client

import backend.team.ahachul_backend.common.dto.GoogleUserInfoDto

interface GoogleMemberClient {

    fun getAccessTokenByCode(code: String): String?

    fun getMemberInfoByAccessToken(accessToken: String): GoogleUserInfoDto?
}