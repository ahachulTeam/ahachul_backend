package backend.team.ahachul_backend.api.lost.application.service

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.application.port.out.CategoryReader
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostFileReader
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostReader
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.application.service.command.*
import backend.team.ahachul_backend.api.lost.domain.entity.CategoryEntity
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostFileEntity
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.dto.ImageDto
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.persistence.SubwayLineReader
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
@Transactional(readOnly = true)
class LostPostService(
    private val lostPostWriter: LostPostWriter,
    private val lostPostReader: LostPostReader,
    private val lostPostFileReader: LostPostFileReader,
    private val lostPostFileService: LostPostFileService,
    private val subwayLineReader: SubwayLineReader,
    private val memberReader: MemberReader,
    private val categoryReader: CategoryReader
): LostPostUseCase {

    override fun getLostPost(id: Long): GetLostPostDto.Response {
        val entity = lostPostReader.getLostPost(id)
        val files = lostPostFileReader.findAllByPostId(id)
        val recommendPosts = getRecommendPosts(entity.subwayLine, entity.category)
        val recommendPostsDto = mapRecommendPostsDto(recommendPosts)
        return GetLostPostDto.Response.of(entity, convertToImageDto(files), recommendPostsDto)
    }

    private fun getRecommendPosts(subwayLine: SubwayLineEntity?, category:CategoryEntity): List<LostPostEntity> {
        if (subwayLine === null ) {
            throw BusinessException(ResponseCode.IMPOSSIBLE_RECOMMEND_LOST_POST)
        }

        val command = GetRecommendLostPostsCommand.from(DEFAULT_RECOMMEND_SIZE, subwayLine, category)
        val recommendPosts = lostPostReader.getRecommendLostPosts(command)

        if (recommendPosts.size >= DEFAULT_RECOMMEND_SIZE) {
            return recommendPosts
        }

        val randomPosts = getRandomPostIfNotDefaultSize(recommendPosts.size, subwayLine, category)
        return recommendPosts.plus(randomPosts)
    }

    private fun getRandomPostIfNotDefaultSize(recommendPostSize: Int,
                                              subwayLine: SubwayLineEntity?, category: CategoryEntity): List<LostPostEntity> {
        val randomCommand = GetRecommendLostPostsCommand.from(
            DEFAULT_RECOMMEND_SIZE - recommendPostSize, subwayLine, category
        )
        return lostPostReader.getRandomLostPosts(randomCommand)
    }

    private fun mapRecommendPostsDto(recommendPosts: List<LostPostEntity>): List<GetLostPostDto.RecommendResponse> {
        val recommendPostsDto = recommendPosts.map { post ->
            val file = lostPostFileReader.findByPostId(post.id)?.file
            GetLostPostDto.RecommendResponse(
                id = post.id,
                title = post.title,
                writer = post.createdBy,
                imgUrl = file?.filePath,
                date = post.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            )
        }
        return recommendPostsDto
    }

    override fun searchLostPosts(command: SearchLostPostCommand): SearchLostPostsDto.Response {
        val subwayLine = command.subwayLineId?.let { subwayLineReader.getSubwayLine(it) }
        val sliceObject = lostPostReader.getLostPosts(GetSliceLostPostsCommand.from(command, subwayLine))

        val lostPosts = sliceObject.content.map {
            val file = lostPostFileReader.findByPostId(it.id)?.file
            SearchLostPostsDto.SearchLost(
                id = it.id,
                title = it.title,
                content = it.content,
                writer = it.member?.nickname,
                createdBy = it.createdBy,
                date = it.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                subwayLine = it.subwayLine?.id,
                status = it.status,
                image = file?.let { f -> ImageDto(f.id, f.filePath) },
                categoryName = it.category.name
            )
        }
        return SearchLostPostsDto.Response(hasNext = sliceObject.hasNext(), posts = lostPosts)
    }

    @Transactional
    override fun createLostPost(command: CreateLostPostCommand): CreateLostPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val member = memberReader.getMember(memberId.toLong())
        val subwayLine = subwayLineReader.getSubwayLine(command.subwayLine)
        val category = categoryReader.getCategoryByName(command.categoryName)

        val entity = lostPostWriter.save(
            LostPostEntity.of(
                command = command,
                member = member,
                subwayLine = subwayLine,
                category = category
            )
        )

        val images = command.imageFiles?.let {
            lostPostFileService.createLostPostFiles(entity, command.imageFiles!!)
        }
        return CreateLostPostDto.Response.from(entity.id, images)
    }

    @Transactional
    override fun updateLostPost(command: UpdateLostPostCommand): UpdateLostPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val entity = lostPostReader.getLostPost(command.id)
        entity.checkMe(memberId)

        val subwayLine = command.subwayLine?.let {
            subwayLineReader.getSubwayLine(it)
        }

        entity.update(command, subwayLine)
        updateImageFiles(command, entity)
        return UpdateLostPostDto.Response.from(entity)
    }

    private fun updateImageFiles(command: UpdateLostPostCommand, post: LostPostEntity) {
        command.imageFiles?.let {
            lostPostFileService.createLostPostFiles(post, it)
        }

        command.removeFileIds?.let {
            lostPostFileService.deleteLostPostFiles(it)
        }
    }

    @Transactional
    override fun deleteLostPost(id: Long): DeleteLostPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val entity = lostPostReader.getLostPost(id)
        entity.checkMe(memberId)
        entity.delete()
        return DeleteLostPostDto.Response.from(entity)
    }

    private fun convertToImageDto(lostPostFiles: List<LostPostFileEntity>): List<ImageDto> {
        return lostPostFiles.map {
            ImageDto.of(
                imageId = it.id,
                imageUrl = it.file!!.filePath
            )
        }
    }

    companion object {
        const val DEFAULT_RECOMMEND_SIZE = 12L
    }
}
