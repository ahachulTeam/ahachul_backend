package backend.team.ahachul_backend.common.domain.lost.persistence

import backend.team.ahachul_backend.common.domain.lost.entity.SubwayLineEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SubwayLineRepository: JpaRepository<SubwayLineEntity, Long> {
}