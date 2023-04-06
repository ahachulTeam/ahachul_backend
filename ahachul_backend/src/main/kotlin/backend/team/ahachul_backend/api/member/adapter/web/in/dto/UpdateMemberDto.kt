package backend.team.ahachul_backend.api.member.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.member.application.port.`in`.command.UpdateMemberCommand
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import org.jetbrains.annotations.NotNull

class UpdateMemberDto {
    data class Request(
            @NotNull
            val nickname: String,
            @NotNull
            val gender: GenderType,
            @NotNull
            val ageRange: String,
    ) {
        fun toCommand(): UpdateMemberCommand {
            return UpdateMemberCommand(
                    nickname = nickname,
                    gender = gender,
                    ageRange = ageRange,
            )
        }
    }

    data class Response(
            val nickname: String,
            val gender: GenderType,
            val ageRange: String,
    ) {
        companion object {
            fun of(nickname: String, gender: GenderType, ageRange: String): Response {
                return Response(
                        nickname = nickname,
                        gender = gender,
                        ageRange = ageRange,
                )
            }
        }
    }
}