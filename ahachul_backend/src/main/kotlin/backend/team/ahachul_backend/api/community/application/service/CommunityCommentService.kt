package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment.*
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityCommentUseCase
import org.springframework.stereotype.Service

@Service
class CommunityCommentService: CommunityCommentUseCase {

    override fun getCommunityComments(): GetCommunityCommentsDto.Response {
        TODO("Not yet implemented")
    }

    override fun createCommunityComment(command: CreateCommunityCommentCommand): CreateCommunityCommentDto.Response {
        TODO("Not yet implemented")
    }

    override fun updateCommunityComment(command: UpdateCommunityCommentCommand): UpdateCommunityCommentDto.Response {
        TODO("Not yet implemented")
    }

    override fun deleteCommunityComment(command: DeleteCommunityCommentCommand): DeleteCommunityCommentDto.Response {
        TODO("Not yet implemented")
    }
}