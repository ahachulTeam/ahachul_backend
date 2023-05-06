package backend.team.ahachul_backend.common.domain.lost.persistence

import backend.team.ahachul_backend.common.domain.lost.entity.SubwayLineEntity

interface SubwayLineReader {

    fun getSubwayLine(id: Long): SubwayLineEntity
}