package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.domain.entity.CommunityCommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommunityCommentRepository: JpaRepository<CommunityCommentEntity, Long> {
}