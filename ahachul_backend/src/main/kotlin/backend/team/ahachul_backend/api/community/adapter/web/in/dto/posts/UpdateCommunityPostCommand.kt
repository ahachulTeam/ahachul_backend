package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts

import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType

class UpdateCommunityPostCommand(
    val id: Long,
    val title: String,
    val content: String,
    val categoryType: CommunityCategoryType,
) {
}