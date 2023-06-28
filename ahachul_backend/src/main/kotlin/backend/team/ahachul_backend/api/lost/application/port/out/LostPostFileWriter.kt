package backend.team.ahachul_backend.api.lost.application.port.out

import backend.team.ahachul_backend.api.lost.domain.entity.LostPostFileEntity

interface LostPostFileWriter {

    fun save(entity: LostPostFileEntity)
}