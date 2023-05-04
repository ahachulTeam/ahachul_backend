package backend.team.ahachul_backend.api.lost.adapter.web.out

import backend.team.ahachul_backend.api.lost.application.port.out.SubwayLineReader
import backend.team.ahachul_backend.api.lost.domain.entity.SubwayLine
import backend.team.ahachul_backend.common.exception.AdapterException
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.stereotype.Component

@Component
class SubwayLinePersistence(
    private val subwayLineRepository: SubwayLineRepository
): SubwayLineReader {

    override fun getSubwayLine(id: Long): SubwayLine {
        return subwayLineRepository.findById(id).orElseThrow {
            throw AdapterException(ResponseCode.INVALID_DOMAIN)
        }
    }
}