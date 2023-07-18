package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post

import backend.team.ahachul_backend.api.community.domain.CommunityPost
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.dto.ImageDto
import backend.team.ahachul_backend.common.model.RegionType
import backend.team.ahachul_backend.common.model.YNType
import java.time.LocalDateTime

class GetCommunityPostDto {

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val categoryType: CommunityCategoryType,
        val hashTags: List<String>,
        val viewCnt: Int,
        val likeCnt: Int,
        val hateCnt: Int,
        val likeYn: YNType,
        val hateYn: YNType,
        val regionType: RegionType,
        val subwayLineId: Long,
        val createdAt: LocalDateTime,
        val createdBy: String,
        val writer: String,
        val images: List<ImageDto>
    ) {
        companion object {
            fun of(communityPost: CommunityPost, hashTags: List<String>, views: Int, images: List<ImageDto>): Response {
                return Response(
                    id = communityPost.id,
                    title = communityPost.title,
                    content = communityPost.content,
                    categoryType = communityPost.categoryType,
                    hashTags = hashTags,
                    viewCnt = views,
                    likeCnt = communityPost.likeCnt.toInt(),
                    hateCnt = communityPost.hateCnt.toInt(),
                    likeYn = YNType.convert(communityPost.likeYn),
                    hateYn = YNType.convert(communityPost.hateYn),
                    regionType = communityPost.regionType,
                    subwayLineId = communityPost.subwayLineId,
                    createdAt = communityPost.createdAt,
                    createdBy = communityPost.createdBy,
                    writer = communityPost.writer,
                    images = images
                )
            }
        }
    }
}