package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostFileEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommunityPostFileRepository: JpaRepository<CommunityPostFileEntity, Long> {
}