package backend.team.ahachul_backend.common.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class KakaoAccessTokenDto @JsonCreator constructor(
        val accessToken: String,
        val tokenType: String,
        val expiresIn: Int,
        val refreshToken: String,
        val refreshTokenExpiresIn: Int
) {

}