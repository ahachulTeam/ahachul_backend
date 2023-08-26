package backend.team.ahachul_backend.api.common.application.port.out

import backend.team.ahachul_backend.api.common.domain.entity.StationEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.exception.DomainException
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.stereotype.Component

@Component
class StationPersistence(
    private val stationRepository: StationRepository
): StationReader {

    override fun findAllBySubwayLine(subwayLine: SubwayLineEntity): List<StationEntity> {
        return stationRepository.findAllBySubwayLineEntityOrderByName(subwayLine) ?:
            throw DomainException(ResponseCode.INVALID_DOMAIN)
    }
}