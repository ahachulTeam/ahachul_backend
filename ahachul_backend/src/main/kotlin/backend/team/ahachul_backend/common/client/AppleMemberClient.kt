package backend.team.ahachul_backend.common.client

interface AppleMemberClient {
    fun verifyIdentityToken(identityToken: String, authCode: String): Boolean

}