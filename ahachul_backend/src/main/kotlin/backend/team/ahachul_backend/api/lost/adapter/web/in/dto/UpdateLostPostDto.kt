package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import org.springframework.web.multipart.MultipartFile

class UpdateLostPostDto {

    data class Request(
        val id: Long,
        val title: String?,
        val content: String?,
        val subwayLine: Long?,
        val status: LostStatus?,
        val removeFileIds: List<Long>? = arrayListOf()
    ) {
        fun toCommand(id: Long, imageFiles: List<MultipartFile>?): UpdateLostPostCommand {
            return UpdateLostPostCommand(
                id = id,
                title = title,
                content = content,
                subwayLine = subwayLine,
                status = status,
                imageFiles = imageFiles,
                removeFileIds = removeFileIds
            )
        }
    }

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val subwayLine: Long?,
        val status: LostStatus
    ) {
        companion object {
            fun from(entity: LostPostEntity): Response {
                return Response(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    subwayLine = entity.subwayLine?.id,
                    status = entity.status
                )
            }
        }
    }
}