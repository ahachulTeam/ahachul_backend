package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment

class UpdateCommunityCommentDto {

    data class Request(
        val content: String,
    ) {
        fun toCommand(id: Long): UpdateCommunityCommentCommand {
            return UpdateCommunityCommentCommand(
                id = id,
                content = content
            )
        }
    }

    data class Response(
        val id: Long,
        val content: String,
    )
}