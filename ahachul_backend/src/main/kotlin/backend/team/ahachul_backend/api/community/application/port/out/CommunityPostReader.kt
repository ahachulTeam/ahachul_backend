package backend.team.ahachul_backend.api.community.application.port.out

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity

interface CommunityPostReader {

    fun getCommunityPost(id: Long): CommunityPostEntity
}