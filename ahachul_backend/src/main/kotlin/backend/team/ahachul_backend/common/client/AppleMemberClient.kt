package backend.team.ahachul_backend.common.client

interface AppleMemberClient {

    fun getAccessTokenByCode(identityToken: String, authCode: String): String

    fun getPublicKey(): String

    fun getUserInfoByAccessToken(accessToken: String): String?

    fun getUserInfoByIdentityToken(accessToken: String): String?
}