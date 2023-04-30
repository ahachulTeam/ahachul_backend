package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts

import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType

class UpdateCommunityPostDto {

    data class Request(
        val title: String,
        val content: String,
        val categoryType: CommunityCategoryType,
    ) {
        fun toCommand(postId: Long): UpdateCommunityPostCommand {
            return UpdateCommunityPostCommand(
                id = postId,
                title = title,
                content = content,
                categoryType = categoryType
            )
        }
    }

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val categoryType: CommunityCategoryType,
    )
}