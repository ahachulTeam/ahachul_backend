package backend.team.ahachul_backend.api.member.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.member.application.port.`in`.command.LoginMemberCommand
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import org.jetbrains.annotations.NotNull

class LoginMemberDto {
    data class Request(
            @NotNull
            val providerCode: String,
            @NotNull
            val providerType: ProviderType
    ) {
        fun toCommand(): LoginMemberCommand {
            return LoginMemberCommand(
                    providerCode = providerCode,
                    providerType = providerType
            )
        }
    }

    data class Response(
            val nickname: String,
            val accessToken: String,
            val accessTokenExpiresIn: String,
            val refreshToken: String,
            val refreshTokenExpiresIn: String,
    )
}