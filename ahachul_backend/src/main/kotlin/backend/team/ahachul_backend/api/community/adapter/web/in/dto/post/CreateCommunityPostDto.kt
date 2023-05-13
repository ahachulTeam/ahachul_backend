package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.model.RegionType

class CreateCommunityPostDto {

    data class Request(
        val title: String,
        val content: String,
        val categoryType: CommunityCategoryType,
        val subwayLineId: Long,
    ) {
        fun toCommand(): CreateCommunityPostCommand {
            return CreateCommunityPostCommand(
                title = title,
                content = content,
                categoryType = categoryType,
                subwayLineId = subwayLineId
            )
        }
    }

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val categoryType: CommunityCategoryType,
        val region: RegionType,
        val subwayLineId: Long
    ) {
        companion object {
            fun from(entity: CommunityPostEntity): Response {
                return Response(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    categoryType = entity.categoryType,
                    region = entity.regionType,
                    subwayLineId = entity.subwayLineEntity.id
                )
            }
        }
    }
}