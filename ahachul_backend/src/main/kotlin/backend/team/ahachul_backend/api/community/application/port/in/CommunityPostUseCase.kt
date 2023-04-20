package backend.team.ahachul_backend.api.community.application.port.`in`

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.*

interface CommunityPostUseCase {

    fun searchCommunityPosts(): SearchCommunityPostDto.Response

    fun getCommunityPost(): GetCommunityPostDto.Response

    fun createCommunityPost(): CreateCommunityPostDto.Response

    fun updateCommunityPost(): UpdateCommunityPostDto.Response

    fun deleteCommunityPost(): DeleteCommunityPostDto.Response
}