package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.application.service.command.SearchLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import org.springframework.data.domain.Pageable

class SearchLostPostsDto {

    data class Request(
        val lostType: LostType,
        val subwayLineId: Long?,
        val origin: LostOrigin?
    ) {
        fun toCommand(pageable: Pageable): SearchLostPostCommand {
            return SearchLostPostCommand(
                pageable = pageable,
                lostType = lostType,
                subwayLineId = subwayLineId,
                lostOrigin = origin
            )
        }
    }

    data class Response(
        val hasNext: Boolean,
        val posts: List<SearchLost>
    )

    data class SearchLost(
        val title: String,
        val content: String,
        val writer: String?,
        val createdBy: String,
        val date: String,
        val subwayLine: Long,
        val chats: Int = 0,
        val status: LostStatus,
        val imgUrl: String = ""
    )
}