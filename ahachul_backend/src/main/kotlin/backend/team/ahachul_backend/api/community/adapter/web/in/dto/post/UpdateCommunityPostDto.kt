package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
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
        val id: Long?,
        val title: String,
        val content: String,
        val categoryType: CommunityCategoryType,
    ) {
        companion object {
            fun from(entity: CommunityPostEntity): Response {
                return Response(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    categoryType = entity.categoryType
                )
            }
        }
    }
}