package backend.team.ahachul_backend.api.community.application.port.`in`

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment.CreateCommunityCommentDto
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment.DeleteCommunityCommentDto
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment.GetCommunityCommentsDto
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment.UpdateCommunityCommentDto

interface CommunityCommentUseCase {

    fun getCommunityComments(): GetCommunityCommentsDto.Response

    fun createCommunityComment(): CreateCommunityCommentDto.Response

    fun updateCommunityComment(): UpdateCommunityCommentDto.Response

    fun deleteCommunityComment(): DeleteCommunityCommentDto.Response
}