package backend.team.ahachul_backend.api.lost.application.port.out

import backend.team.ahachul_backend.api.lost.domain.entity.SubwayLine

interface SubwayLineReader {

    fun getSubwayLine(id: Long): SubwayLine
}