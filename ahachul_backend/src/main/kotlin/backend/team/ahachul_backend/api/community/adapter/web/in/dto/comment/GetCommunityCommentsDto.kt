package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment

import java.time.LocalDateTime

class GetCommunityCommentsDto {

    data class Response(
        val comments: List<CommunityComment>
    )

    data class CommunityComment(
        val id: Long,
        val upperCommentId: Long?,
        val content: String,
        val createdAt: LocalDateTime,
        val createdBy: String,
        val writer: String,
    )
}