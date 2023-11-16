package backend.team.ahachul_backend.api.common.application.port.out

import backend.team.ahachul_backend.api.common.domain.entity.LineStationEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface LineStationRepository: JpaRepository<LineStationEntity, Long> {

    @EntityGraph(attributePaths = ["station", "subwayLine"])
    override fun findAll(): List<LineStationEntity>
}
