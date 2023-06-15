package backend.team.ahachul_backend.api.community.application.port.out

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.SearchCommunityPostCommand
import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import org.springframework.data.domain.Slice

interface CommunityPostReader {

    fun getCommunityPost(id: Long): CommunityPostEntity

    fun searchCommunityPosts(command: SearchCommunityPostCommand): Slice<CommunityPostEntity>
}