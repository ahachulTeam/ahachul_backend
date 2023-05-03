package backend.team.ahachul_backend.api.lost.application.service

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service

@Service
class LostPostService(
    private val lostPostWriter: LostPostWriter,
    private val memberReader: MemberReader
): LostPostUseCase {

    override fun getLostPost(): GetLostPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun searchLostPosts(): SearchLostPostsDto.Response {
        TODO("Not yet implemented")
    }

    override fun createLostPost(command: CreateLostPostCommand): CreateLostPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val member = memberReader.getMember(memberId.toLong())
        val entity = lostPostWriter.save(LostPostEntity.of(command, member))
        return CreateLostPostDto.Response.from(entity.id!!)
    }

    override fun updateLostPost(): UpdateLostPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun deleteLostPost(): DeleteLostPostDto.Response {
        TODO("Not yet implemented")
    }
}