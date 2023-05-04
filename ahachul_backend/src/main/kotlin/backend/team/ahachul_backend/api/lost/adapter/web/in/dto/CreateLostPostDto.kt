package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import org.jetbrains.annotations.NotNull

class CreateLostPostDto{

    data class Request(
        @NotNull val title: String,
        @NotNull val content: String,
        @NotNull val subwayLine: Long,
        @NotNull val lostType: LostType,
        val imgUrls: List<String>?
    ) {
        fun toCommand(): CreateLostPostCommand {
            return CreateLostPostCommand(
                title = title,
                content = content,
                subwayLine = subwayLine,
                lostType = lostType,
                imgUrls = imgUrls
            )
        }
    }

    data class Response(
        val id: Long
    ) {
        companion object {
            fun from(id: Long): Response {
                return Response(id)
            }
        }
    }
}