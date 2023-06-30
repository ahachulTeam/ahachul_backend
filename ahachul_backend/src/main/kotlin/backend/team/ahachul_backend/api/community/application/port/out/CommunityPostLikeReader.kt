package backend.team.ahachul_backend.api.community.application.port.out

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostLikeEntity

interface CommunityPostLikeReader {

    fun find(postId: Long, memberId: Long): CommunityPostLikeEntity?

    fun exist(postId: Long, memberId: Long): Boolean
}