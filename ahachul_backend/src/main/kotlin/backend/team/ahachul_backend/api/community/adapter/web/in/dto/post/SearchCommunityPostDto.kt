package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.model.RegionType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import java.time.LocalDateTime

class SearchCommunityPostDto {

    data class Request(
        val categoryType: CommunityCategoryType?,
        val subwayLineId: Long?,
        val content: String?,
    ) {
        fun toCommand(pageable: Pageable): SearchCommunityPostCommand {
            return SearchCommunityPostCommand(
                categoryType = categoryType,
                subwayLineId = subwayLineId,
                content = content,
                pageable = pageable
            )
        }
    }

    data class Response(
        val hasNext: Boolean,
        val posts: List<CommunityPost>,
    ) {
        companion object {
            fun of(entities: Slice<CommunityPostEntity>, views: List<Int>): Response {
                return Response(
                    hasNext = entities.hasNext(),
                    posts = entities
                        .mapIndexed { idx, entity -> CommunityPost.of(entity, views[idx]) }
                        .toList()
                )
            }
        }
    }

    data class CommunityPost(
        val id: Long,
        val title: String,
        val categoryType: CommunityCategoryType,
        val views: Int,
        val likes: Int,
        val region: RegionType,
        val createdAt: LocalDateTime,
        val createdBy: String,
        val writer: String
    ) {
        companion object {
            fun of(entity: CommunityPostEntity, views: Int): CommunityPost {
                return CommunityPost(
                    id = entity.id,
                    title = entity.title,
                    categoryType = entity.categoryType,
                    views = views,
                    likes = 0, // TODO
                    region = entity.regionType,
                    createdAt = entity.createdAt,
                    createdBy = entity.createdBy,
                    writer = entity.member!!.nickname!!
                )
            }
        }
    }
}