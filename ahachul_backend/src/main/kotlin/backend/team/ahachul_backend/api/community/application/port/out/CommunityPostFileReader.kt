package backend.team.ahachul_backend.api.community.application.port.out

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostFileEntity

interface CommunityPostFileReader {

    fun findAllByPostId(postId: Long): List<CommunityPostFileEntity>
}