package backend.team.ahachul_backend.api.common.application.port.out

import backend.team.ahachul_backend.api.common.adapter.domain.entity.StationEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StationRepository: JpaRepository<StationEntity, Long> {

    fun findAllBySubwayLineEntity(subwayLine: SubwayLineEntity): List<StationEntity>?
}
