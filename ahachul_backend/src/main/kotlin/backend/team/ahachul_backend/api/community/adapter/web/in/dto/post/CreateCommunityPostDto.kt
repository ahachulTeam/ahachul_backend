package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.model.RegionType
import org.springframework.web.multipart.MultipartFile

class CreateCommunityPostDto {

    data class Request(
        val title: String,
        val content: String,
        val categoryType: String,
        val hashTags: List<String>,
        val subwayLineId: String,
        val imageFiles: List<MultipartFile> = arrayListOf()
    ) {
        fun toCommand(): CreateCommunityPostCommand {
            return CreateCommunityPostCommand(
                title = title,
                content = content,
                categoryType = CommunityCategoryType.ISSUE,
                hashTags = arrayListOf(),
                subwayLineId = 1,
                imageFiles = imageFiles
            )
        }
    }

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val categoryType: CommunityCategoryType,
        val region: RegionType,
        val subwayLineId: Long,
        val imageUrls: List<String> = arrayListOf()
    ) {
        companion object {
            fun from(entity: CommunityPostEntity, imageUrls: List<String>): Response {
                return Response(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    categoryType = entity.categoryType,
                    region = entity.regionType,
                    subwayLineId = entity.subwayLineEntity.id,
                    imageUrls = imageUrls
                )
            }
        }
    }
}