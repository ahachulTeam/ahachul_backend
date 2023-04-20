package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto

class CreateCommunityCommentDto {

    data class Request(
        val postId: Long,
        val upperCommentId: Long?,
        val content: String,
    )

    data class Response(
        val id: Long,
        val upperCommentId: Long?,
        val content: String,
    )
}