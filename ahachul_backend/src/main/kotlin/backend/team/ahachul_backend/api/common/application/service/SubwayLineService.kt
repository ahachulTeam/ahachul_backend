package backend.team.ahachul_backend.api.common.application.service

import backend.team.ahachul_backend.api.common.adapter.`in`.dto.SearchSubwayLineDto
import backend.team.ahachul_backend.api.common.adapter.`in`.dto.Station
import backend.team.ahachul_backend.api.common.adapter.`in`.dto.SubwayLine
import backend.team.ahachul_backend.api.common.application.port.`in`.SubwayLineUseCase
import backend.team.ahachul_backend.api.common.application.port.out.LineStationReader
import backend.team.ahachul_backend.api.common.domain.entity.LineStationEntity
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SubwayLineService(
    private val lineStationReader: LineStationReader
): SubwayLineUseCase {

    @Cacheable("subwayLines")
    override fun searchSubwayLines(): SearchSubwayLineDto.Response {
        return SearchSubwayLineDto.Response(
            lineStationReader.findAll()
                .groupBy { it.subwayLine }
                .map { SubwayLine.of(it.key, mapStationsDto(it.value)) }
                .toList()
        )
    }

    private fun mapStationsDto(stations: List<LineStationEntity>): List<Station> {
        return stations.map { Station.of(it.station) }
    }
}
