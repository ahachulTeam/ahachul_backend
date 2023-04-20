package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType

class CreateCommunityPostDto {

    data class Request(
        val title: String?,
        val content: String?,
        val categoryType: CommunityCategoryType?,
    )

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val categoryType: CommunityCategoryType,
        val region: String,
    )
}