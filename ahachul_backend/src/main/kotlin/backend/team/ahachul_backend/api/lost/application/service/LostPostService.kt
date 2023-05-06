package backend.team.ahachul_backend.api.lost.application.service

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostReader
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.application.port.out.SubwayLineReader
import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.GetSliceLostPostsCommand
import backend.team.ahachul_backend.api.lost.application.service.command.SearchLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
@Transactional(readOnly = true)
class LostPostService(
    private val lostPostWriter: LostPostWriter,
    private val lostPostReader: LostPostReader,
    private val subwayLineReader: SubwayLineReader,
    private val memberReader: MemberReader
): LostPostUseCase {

    override fun getLostPost(id: Long): GetLostPostDto.Response {
        val entity = lostPostReader.getLostPost(id)
        return GetLostPostDto.Response.from(entity);
    }

    override fun searchLostPosts(command: SearchLostPostCommand): SearchLostPostsDto.Response {
        val subwayLine = command.subwayLine?.let { subwayLineReader.getSubwayLine(it) }
        val sliceObject = lostPostReader.getLostPosts(GetSliceLostPostsCommand.from(command, subwayLine))
        val lostPosts = sliceObject.content.map {
            SearchLostPostsDto.SearchLost(
                title = it.title,
                content = it.content,
                writer = it.member.nickname!!,
                date = it.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                subwayLine = it.subwayLine.id,
                status = it.status
            )
        }
        return SearchLostPostsDto.Response(hasNext = sliceObject.hasNext(), contents = lostPosts)
    }

    @Transactional
    override fun createLostPost(command: CreateLostPostCommand): CreateLostPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val member = memberReader.getMember(memberId.toLong())
        val subwayLine = subwayLineReader.getSubwayLine(command.subwayLine)

        val entity = lostPostWriter.save(
            LostPostEntity.of(
                command = command,
                member = member,
                subwayLine = subwayLine
            )
        )
        return CreateLostPostDto.Response.from(entity.id)
    }

    @Transactional
    override fun updateLostPost(id: Long, command: UpdateLostPostCommand): UpdateLostPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val entity = lostPostReader.getLostPost(id)
        entity.checkMe(memberId)

        val subwayLine = command.subwayLine?.let {
            subwayLineReader.getSubwayLine(it)
        }

        entity.update(command = command, subwayLine = subwayLine)
        return UpdateLostPostDto.Response.from(entity)
    }

    @Transactional
    override fun deleteLostPost(id: Long): DeleteLostPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val entity = lostPostReader.getLostPost(id)
        entity.checkMe(memberId)
        entity.delete()
        return DeleteLostPostDto.Response.from(entity)
    }
}