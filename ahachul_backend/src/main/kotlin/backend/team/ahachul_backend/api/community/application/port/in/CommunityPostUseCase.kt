package backend.team.ahachul_backend.api.community.application.port.`in`

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts.*

interface CommunityPostUseCase {

    fun searchCommunityPosts(): SearchCommunityPostDto.Response

    fun getCommunityPost(command: GetCommunityPostCommand): GetCommunityPostDto.Response

    fun createCommunityPost(command: CreateCommunityPostCommand): CreateCommunityPostDto.Response

    fun updateCommunityPost(command: UpdateCommunityPostCommand): UpdateCommunityPostDto.Response

    fun deleteCommunityPost(command: DeleteCommunityPostCommand): DeleteCommunityPostDto.Response
}