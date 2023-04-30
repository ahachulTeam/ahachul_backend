package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts.*
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityPostUseCase
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostWriter
import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommunityPostService(
    private val communityPostWriter: CommunityPostWriter,
    private val communityPostReader: CommunityPostReader
): CommunityPostUseCase {

    override fun searchCommunityPosts(): SearchCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun getCommunityPost(command: GetCommunityPostCommand): GetCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun createCommunityPost(command: CreateCommunityPostCommand): CreateCommunityPostDto.Response {
        val entity = communityPostWriter.save(CommunityPostEntity.from(command))
        return CreateCommunityPostDto.Response.from(entity)
    }

    override fun updateCommunityPost(command: UpdateCommunityPostCommand): UpdateCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun deleteCommunityPost(command: DeleteCommunityPostCommand): DeleteCommunityPostDto.Response {
        TODO("Not yet implemented")
    }

}