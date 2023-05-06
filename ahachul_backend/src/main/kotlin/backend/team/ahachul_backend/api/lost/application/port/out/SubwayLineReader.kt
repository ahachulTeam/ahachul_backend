package backend.team.ahachul_backend.api.lost.application.port.out

import backend.team.ahachul_backend.api.lost.domain.entity.SubwayLineEntity

interface SubwayLineReader {

    fun getSubwayLine(id: Long): SubwayLineEntity
}