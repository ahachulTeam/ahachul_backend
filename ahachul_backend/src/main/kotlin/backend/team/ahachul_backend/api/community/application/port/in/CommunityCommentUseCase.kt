package backend.team.ahachul_backend.api.community.application.port.`in`

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.*

interface CommunityCommentUseCase {

    fun getCommunityComments(): GetCommunityCommentsDto.Response

    fun createCommunityComment(): CreateCommunityCommentDto.Response

    fun updateCommunityComment(): UpdateCommunityCommentDto.Response

    fun deleteCommunityComment(): DeleteCommunityCommentDto.Response
}