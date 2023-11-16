package backend.team.ahachul_backend.api.common.application.port.out

import backend.team.ahachul_backend.api.common.domain.entity.LineStationEntity

interface LineStationReader {

    fun findAll(): List<LineStationEntity>
}
