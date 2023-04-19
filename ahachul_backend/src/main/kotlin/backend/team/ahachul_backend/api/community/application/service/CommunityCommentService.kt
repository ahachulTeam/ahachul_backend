package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.CreateCommunityCommentDto
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.DeleteCommunityCommentDto
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.GetCommunityCommentsDto
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.UpdateCommunityCommentDto
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityCommentUseCase
import org.springframework.stereotype.Service

@Service
class CommunityCommentService: CommunityCommentUseCase {

    override fun getCommunityComments(): GetCommunityCommentsDto.Response {
        TODO("Not yet implemented")
    }

    override fun createCommunityComment(): CreateCommunityCommentDto.Response {
        TODO("Not yet implemented")
    }

    override fun updateCommunityComment(): UpdateCommunityCommentDto.Response {
        TODO("Not yet implemented")
    }

    override fun deleteCommunityComment(): DeleteCommunityCommentDto.Response {
        TODO("Not yet implemented")
    }
}