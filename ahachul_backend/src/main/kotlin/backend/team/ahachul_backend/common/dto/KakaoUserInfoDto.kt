package backend.team.ahachul_backend.common.dto

data class KakaoUserInfoDto(
        val sub: String,
        val nickname: String,
        val email: String,
        val gender: String
) {
}