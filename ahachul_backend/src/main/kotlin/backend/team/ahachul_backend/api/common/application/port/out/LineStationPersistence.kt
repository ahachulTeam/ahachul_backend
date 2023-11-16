package backend.team.ahachul_backend.api.common.application.port.out

import backend.team.ahachul_backend.api.common.domain.entity.LineStationEntity
import org.springframework.stereotype.Component

@Component
class LineStationPersistence(
    private val lineStationRepository: LineStationRepository
): LineStationReader {

    override fun findAll(): List<LineStationEntity> {
        return lineStationRepository.findAll()
    }

}
