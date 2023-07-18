package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.dto.ImageDto
import backend.team.ahachul_backend.common.model.RegionType
import org.springframework.data.domain.Pageable
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
        val content: String,
        val categoryType: CommunityCategoryType,
        val hashTags: List<String>,
        val commentCnt: Int,
        val viewCnt: Int,
        val likeCnt: Int,
        val regionType: RegionType,
        val subwayLineId: Long,
        val createdAt: LocalDateTime,
        val createdBy: String,
        val writer: String,
        val image: ImageDto?,
    ) {
        companion object {
            fun of(entity: CommunityPostEntity, image: ImageDto?, views: Int, commentCnt: Int,  likeCnt: Int): CommunityPost {
                return CommunityPost(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    categoryType = entity.categoryType,
                    hashTags = entity.communityPostHashTags.map { it.hashTag.name },
                    commentCnt = commentCnt,
                    viewCnt = views,
                    likeCnt = likeCnt,
                    regionType = entity.regionType,
                    subwayLineId = entity.subwayLineEntity.id,
                    createdAt = entity.createdAt,
                    createdBy = entity.createdBy,
                    writer = entity.member!!.nickname!!,
                    image = image,
                )
            }
        }
    }
}