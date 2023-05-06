package backend.team.ahachul_backend.api.lost.adapter.web.out

import backend.team.ahachul_backend.api.lost.domain.entity.SubwayLineEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SubwayLineRepository: JpaRepository<SubwayLineEntity, Long> {
}