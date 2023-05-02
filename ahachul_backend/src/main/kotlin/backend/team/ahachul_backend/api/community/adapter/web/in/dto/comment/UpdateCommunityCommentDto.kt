package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment

class UpdateCommunityCommentDto {

    data class Request(
        val content: String,
    )

    data class Response(
        val id: Long,
        val content: String,
    )
}