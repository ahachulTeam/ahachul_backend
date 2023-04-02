package backend.team.ahachul_backend.common.dto

data class KakaoAccessTokenDto(
        val tokenType: String,
        val accessToken: String,
        val expiresIn: Int,
        val refreshToken: String,
        val refreshTokenExpiresIn: Int
) {

}