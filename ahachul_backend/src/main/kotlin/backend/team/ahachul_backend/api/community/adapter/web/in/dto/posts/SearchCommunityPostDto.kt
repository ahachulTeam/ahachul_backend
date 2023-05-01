package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts

import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import java.time.LocalDateTime

class SearchCommunityPostDto {

    data class Request(
        val categoryType: CommunityCategoryType?,
        val subwayLine: String?,
        val title: String?,
        val content: String?,
        val hashTag: String?
    )

    data class Response(
        val posts: List<CommunityPost>
    )

    data class CommunityPost(
        val id: Long,
        val title: String,
        val categoryType: CommunityCategoryType,
        val views: Int,
        val likes: Int,
        val region: String,
        val createdAt: LocalDateTime,
        val createdBy: String
    )
}