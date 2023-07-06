package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.common.dto.ImageDto
import java.time.format.DateTimeFormatter

class GetLostPostDto {

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val writer: String?,
        val createdBy: String?,
        val date: String,
        val subwayLine: Long?,
        val chats: Int = 0,
        val status: LostStatus,
        val storage: String?,
        val storageNumber: String?,
        val pageUrl: String?,
        val images: List<ImageDto>?
    ) {
        companion object {
            fun from(entity: LostPostEntity, images: List<ImageDto>): Response {
                return Response(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    writer = entity.member?.nickname,
                    createdBy = entity.member?.createdBy,
                    date = entity.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    subwayLine = entity.subwayLine?.id,
                    status = entity.status,
                    storage = entity.storage,
                    storageNumber = entity.storageNumber,
                    pageUrl = entity.pageUrl,
                    images = images
                )
            }
        }
    }
}
