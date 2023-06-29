package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.dto.ImageDto
import backend.team.ahachul_backend.common.model.RegionType
import org.springframework.web.multipart.MultipartFile

class CreateCommunityPostDto {

    data class Request(
        val title: String,
        val content: String,
        val categoryType: CommunityCategoryType,
        val hashTags: List<String> = listOf(),
        val subwayLineId: String,
        val imageFiles: List<MultipartFile> = listOf()
    ) {
        fun toCommand(): CreateCommunityPostCommand {
            return CreateCommunityPostCommand(
                title = title,
                content = content,
                categoryType = categoryType,
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
        val images: List<ImageDto> = arrayListOf()
    ) {
        companion object {
            fun of(entity: CommunityPostEntity, images: List<ImageDto>): Response {
                return Response(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    categoryType = entity.categoryType,
                    region = entity.regionType,
                    subwayLineId = entity.subwayLineEntity.id,
                    images = images
                )
            }
        }
    }
}