package backend.team.ahachul_backend.api.member.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import org.jetbrains.annotations.NotNull

class SignupMemberDto {
    data class Request(
            @NotNull
            val providerType: ProviderType,
            @NotNull
            val providerCode: String,
            @NotNull
            val nickname: String,
            @NotNull
            val gender: String,
            @NotNull
            val ageRange: String
    )

    data class Response(
            val nickname: String,
            val accessToken: String,
            val accessTokenExpiresIn: String,
            val refreshToken: String,
            val refreshTokenExpiresIn: String,
    )
}