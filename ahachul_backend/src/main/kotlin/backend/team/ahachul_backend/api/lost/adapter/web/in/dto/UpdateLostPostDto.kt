package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus

class UpdateLostPostDto {

    data class Request(
        val id: Long,
        val title: String?,
        val content: String?,
        val imgUrls: List<String>?,
        val subwayLine: Long?,
        val status: LostStatus?
    ) {
        fun toCommand(id: Long): UpdateLostPostCommand {
            return UpdateLostPostCommand(
                id = id,
                title = title,
                content = content,
                imgUrls = imgUrls,
                subwayLine = subwayLine,
                status = status
            )
        }
    }

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val subwayLine: Long,
        val status: LostStatus
    ) {
        companion object {
            fun from(entity: LostPostEntity): Response {
                return Response(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    subwayLine = entity.subwayLine.id,
                    status = entity.status
                )
            }
        }
    }
}