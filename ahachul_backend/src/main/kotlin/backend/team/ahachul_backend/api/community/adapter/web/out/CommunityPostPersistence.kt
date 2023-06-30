package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.SearchCommunityPostCommand
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostWriter
import backend.team.ahachul_backend.api.community.domain.CommunityPost
import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.common.exception.AdapterException
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Component

@Component
class CommunityPostPersistence(
    private val repository: CommunityPostRepository,
    private val customRepository: CustomCommunityPostRepository,
): CommunityPostReader, CommunityPostWriter {

    override fun save(entity: CommunityPostEntity): CommunityPostEntity {
        return repository.save(entity)
    }

    override fun getCommunityPost(id: Long): CommunityPostEntity {
        return repository.findById(id)
            .orElseThrow { throw AdapterException(ResponseCode.INVALID_DOMAIN) }
    }

    override fun getByCustom(postId: Long, memberId: String?): CommunityPost {
        return customRepository.getByCustom(postId, memberId) ?: throw AdapterException(ResponseCode.INVALID_DOMAIN)
    }

    override fun searchCommunityPosts(command: SearchCommunityPostCommand): Slice<CommunityPostEntity> {
        return customRepository.searchCommunityPosts(command)
    }
}