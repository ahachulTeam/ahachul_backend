package backend.team.ahachul_backend.api.common.application.port.out

import backend.team.ahachul_backend.api.common.domain.entity.SubwayLineStationEntity
import org.springframework.stereotype.Component

@Component
class SubwayLineStationPersistence(
    private val subwayLineStationRepository: SubwayLineStationRepository
): SubwayLineStationReader {

    override fun findAll(): List<SubwayLineStationEntity> {
        return subwayLineStationRepository.findAll()
    }

}
