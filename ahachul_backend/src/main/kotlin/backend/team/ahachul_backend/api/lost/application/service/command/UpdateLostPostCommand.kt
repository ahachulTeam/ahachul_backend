package backend.team.ahachul_backend.api.lost.application.service.command

import backend.team.ahachul_backend.api.lost.domain.model.LostStatus

class UpdateLostPostCommand (
    val title: String?,
    val content: String?,
    val subwayLine: Long?,
    val status: LostStatus?,
    val imgUrls: List<String>?,
) {
}