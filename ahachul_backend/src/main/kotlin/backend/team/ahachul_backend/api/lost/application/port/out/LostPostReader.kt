package backend.team.ahachul_backend.api.lost.application.port.out

import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity

interface LostPostReader {

    fun getLostPost(id: Long): LostPostEntity
}