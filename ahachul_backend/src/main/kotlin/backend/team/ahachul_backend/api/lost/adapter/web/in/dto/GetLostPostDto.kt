package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import java.time.format.DateTimeFormatter

class GetLostPostDto {

    data class Response(
        val title: String,
        val content: String,
        val writer: String,
        val date: String,
        val subwayLine: Long,
        val chats: Int = 0,
        val status: LostStatus,
        val imgUrls: List<String>? = listOf(),
        val storage: String?,
        val storageNumber: String?
    ) {
        companion object {
            fun from(entity: LostPostEntity): Response {
                return Response(
                    title = entity.title,
                    content = entity.content,
                    writer = entity.member.nickname!!,
                    date = entity.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    subwayLine = entity.subwayLine.id,
                    status = entity.status,
                    storage = entity.storage,
                    storageNumber = entity.storageNumber
                )
            }
        }
    }
}