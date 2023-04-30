package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts.*
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityPostUseCase
import org.springframework.stereotype.Service

@Service
class CommunityPostService: CommunityPostUseCase {

    override fun searchCommunityPosts(): SearchCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun getCommunityPost(command: GetCommunityPostCommand): GetCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun createCommunityPost(command: CreateCommunityPostCommand): CreateCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun updateCommunityPost(command: UpdateCommunityPostCommand): UpdateCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun deleteCommunityPost(command: DeleteCommunityPostCommand): DeleteCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

}