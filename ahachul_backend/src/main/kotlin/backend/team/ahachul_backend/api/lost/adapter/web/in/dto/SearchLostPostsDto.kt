package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.model.LostStatus

class SearchLostPostsDto {

    data class Response(
        val posts: List<SearchLost>
    )

    data class SearchLost(
        val title: String,
        val content: String,
        val writer: String,
        val date: String,
        val lostLine: String,
        val chats: Int,
        val status: LostStatus,
        val imgUrl: String
    )
}