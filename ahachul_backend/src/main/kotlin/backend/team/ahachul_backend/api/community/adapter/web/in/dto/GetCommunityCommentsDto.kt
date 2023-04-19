package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto

class GetCommunityCommentsDto {

    data class Response(
        val comments: List<CommunityComment>
    )

    data class CommunityComment(
        val id: Long,
        val upperCommentId: Long,
        val content: String,
    )
}