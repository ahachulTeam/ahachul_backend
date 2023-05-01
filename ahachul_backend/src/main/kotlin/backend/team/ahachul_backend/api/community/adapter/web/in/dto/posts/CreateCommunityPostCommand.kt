package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts

import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType

class CreateCommunityPostCommand(
    val title: String,
    val content: String,
    val categoryType: CommunityCategoryType,
) {
}