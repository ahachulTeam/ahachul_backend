package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.model.LostStatus

class GetLostPostDto {
    data class AllResponse(
        val lostList: List<Response>
    )

    data class Response(
        val title: String,
        val content: String,
        val writer: String,
        val date: String,
        val lostLine: String,
        val chats: Int,
        val status: LostStatus,
        val imgUrls: List<String>,
        val storage: String,
        val storageNumber: String
    )
}