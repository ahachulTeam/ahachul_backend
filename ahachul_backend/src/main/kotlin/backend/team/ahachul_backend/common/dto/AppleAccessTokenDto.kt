package backend.team.ahachul_backend.common.dto

data class AppleAccessTokenDto(
        val accessToken: String,
        val tokenType: String,
        val expiresIn: Int,
        val refreshToken: String,
        val idToken: String
) {
}