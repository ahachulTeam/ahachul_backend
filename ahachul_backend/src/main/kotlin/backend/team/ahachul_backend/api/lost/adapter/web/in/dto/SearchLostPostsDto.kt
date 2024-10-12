package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.application.service.command.`in`.SearchLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType

class SearchLostPostsDto {

    data class Request(
        val lostType: LostType,
        val lostOrigin: LostOrigin,
        val subwayLineId: Long?,
        val category: String?,
        val keyword: String?,
    ) {
        fun toCommand(pageToken: String?, pageSize: Int): SearchLostPostCommand {
            return SearchLostPostCommand(
                lostType = lostType,
                lostOrigin = lostOrigin,
                subwayLineId = subwayLineId,
                keyword = keyword,
                category = category,
                pageToken = pageToken,
                pageSize = pageSize
            )
        }
    }

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val writer: String?,
        val createdBy: String,
        val createdAt: String,
        val subwayLineId: Long?,
        val chatCnt: Int = 0,
        val status: LostStatus,
        val image: String?,
        val categoryName: String?
    )
}
