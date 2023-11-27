package backend.team.ahachul_backend.api.common.application.port.out

import backend.team.ahachul_backend.api.common.domain.entity.StationEntity
import backend.team.ahachul_backend.api.common.domain.entity.SubwayLineStationEntity
import org.springframework.data.jpa.repository.EntityGraph

interface SubwayLineStationReader {

    @EntityGraph(attributePaths = ["subwayLine"])
    fun findByStation(station: StationEntity): List<SubwayLineStationEntity>

    fun findAll(): List<SubwayLineStationEntity>
}
