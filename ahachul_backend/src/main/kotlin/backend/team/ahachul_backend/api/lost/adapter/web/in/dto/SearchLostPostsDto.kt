package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.model.LostStatus

class SearchLostPostsDto {

    data class Response(
        val hasNext: Boolean,
        val contents: List<SearchLost>
    )

    data class SearchLost(
        val title: String,
        val content: String,
        val writer: String,
        val createdBy: String,
        val date: String,
        val subwayLine: Long,
        val chats: Int = 0,
        val status: LostStatus,
        val imgUrl: String = ""
    )
}