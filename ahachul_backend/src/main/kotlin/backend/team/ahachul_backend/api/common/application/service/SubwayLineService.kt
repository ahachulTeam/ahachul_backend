package backend.team.ahachul_backend.api.common.application.service

import backend.team.ahachul_backend.api.common.adapter.`in`.dto.SearchSubwayLineDto
import backend.team.ahachul_backend.api.common.adapter.`in`.dto.SubwayLine
import backend.team.ahachul_backend.api.common.application.port.`in`.SubwayLineUseCase
import backend.team.ahachul_backend.common.persistence.SubwayLineRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SubwayLineService(
    private val subwayLineRepository: SubwayLineRepository,
): SubwayLineUseCase {

    override fun searchSubwayLines(): SearchSubwayLineDto.Response {
        return SearchSubwayLineDto.Response(
            subwayLineRepository.findAll()
                .stream()
                .map { subwayLine -> SubwayLine.of(subwayLine) }
                .toList()
        )
    }
}