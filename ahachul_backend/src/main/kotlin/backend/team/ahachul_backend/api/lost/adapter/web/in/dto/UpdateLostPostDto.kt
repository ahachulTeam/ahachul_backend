package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.model.LostStatus

class UpdateLostPostDto {

    data class Request(
        val title: String?,
        val content: String?,
        val imgUrls: List<String>?,
        val lostLine: String?,
        val status: LostStatus?
    )

    data class Response(
        val id: Long
    )
}