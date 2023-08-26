package backend.team.ahachul_backend.api.common.application.service

import backend.team.ahachul_backend.api.common.adapter.`in`.dto.SearchSubwayLineDto
import backend.team.ahachul_backend.api.common.adapter.`in`.dto.StationDto
import backend.team.ahachul_backend.api.common.adapter.`in`.dto.SubwayLine
import backend.team.ahachul_backend.api.common.application.port.`in`.SubwayLineUseCase
import backend.team.ahachul_backend.api.common.application.port.out.StationReader
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.persistence.SubwayLineRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SubwayLineService(
    private val subwayLineRepository: SubwayLineRepository,
    private val stationReader: StationReader
): SubwayLineUseCase {

    override fun searchSubwayLines(): SearchSubwayLineDto.Response {
        return SearchSubwayLineDto.Response(
            subwayLineRepository.findAll()
                .stream()
                .map { subwayLine -> SubwayLine.of(subwayLine, getStations(subwayLine)) }
                .toList()
        )
    }

    private fun getStations(subwayLine: SubwayLineEntity): List<StationDto> {
        val stations = stationReader.findAllBySubwayLine(subwayLine)
        return stations.map {
            StationDto(
                id = it.id,
                name = it.stationName
            )
        }
    }
}
