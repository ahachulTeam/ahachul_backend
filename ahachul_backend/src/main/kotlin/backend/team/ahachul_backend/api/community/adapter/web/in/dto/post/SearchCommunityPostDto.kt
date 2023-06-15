package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.dto.ImageDto
import backend.team.ahachul_backend.common.model.RegionType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import java.time.LocalDateTime

class SearchCommunityPostDto {

    data class Request(
        val categoryType: CommunityCategoryType?,
        val subwayLineId: Long?,
        val content: String?,
        val hashTag: String?,
    ) {
        fun toCommand(pageable: Pageable): SearchCommunityPostCommand {
            return SearchCommunityPostCommand(
                categoryType = categoryType,
                subwayLineId = subwayLineId,
                content = content,
                hashTag = hashTag,
                pageable = pageable
            )
        }
    }

    data class Response(
        val hasNext: Boolean,
        val posts: List<CommunityPost>,
    ) {
        companion object {
            fun of(hasNext: Boolean, posts: List<CommunityPost>): Response {
                return Response(
                    hasNext = hasNext,
                    posts = posts
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
        val writer: String,
        val image: ImageDto?,
    ) {
        companion object {
            fun of(entity: CommunityPostEntity, image: ImageDto?, views: Int): CommunityPost {
                return CommunityPost(
                    id = entity.id,
                    title = entity.title,
                    categoryType = entity.categoryType,
                    views = views,
                    likes = 0, // TODO
                    region = entity.regionType,
                    createdAt = entity.createdAt,
                    createdBy = entity.createdBy,
                    writer = entity.member!!.nickname!!,
                    image = image,
                )
            }
        }
    }
}