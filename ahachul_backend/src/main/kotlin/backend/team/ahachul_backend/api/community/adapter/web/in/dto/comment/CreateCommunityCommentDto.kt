package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment

class CreateCommunityCommentDto {

    data class Request(
        val postId: Long,
        val upperCommentId: Long?,
        val content: String,
    ) {
        fun toCommand(): CreateCommunityCommentCommand {
            return CreateCommunityCommentCommand(
                postId = postId,
                upperCommentId = upperCommentId,
                content = content
            )
        }
    }

    data class Response(
        val id: Long,
        val upperCommentId: Long?,
        val content: String,
    )
}