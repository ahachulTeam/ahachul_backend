package backend.team.ahachul_backend.common.domain.lost.persistence

import backend.team.ahachul_backend.common.domain.lost.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.exception.AdapterException
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.stereotype.Component

@Component
class SubwayLinePersistence(
    private val subwayLineRepository: SubwayLineRepository
): SubwayLineReader {

    override fun getSubwayLine(id: Long): SubwayLineEntity {
        return subwayLineRepository.findById(id).orElseThrow {
            throw AdapterException(ResponseCode.INVALID_DOMAIN)
        }
    }
}