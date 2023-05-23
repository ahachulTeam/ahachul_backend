package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostHashTagEntity
import org.springframework.data.jpa.repository.JpaRepository


interface CommunityPostHashTagRepository: JpaRepository<CommunityPostHashTagEntity, Long> {

}