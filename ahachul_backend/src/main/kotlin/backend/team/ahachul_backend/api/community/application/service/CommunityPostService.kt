package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityPostUseCase
import org.springframework.stereotype.Service

@Service
class CommunityPostService: CommunityPostUseCase {

    override fun searchCommunityPosts(): SearchCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun getCommunityPost(): GetCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun createCommunityPost(): CreateCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun updateCommunityPost(): UpdateCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun deleteCommunityPost(): DeleteCommunityPostDto.Response {
        TODO("Not yet implemented")
    }
}