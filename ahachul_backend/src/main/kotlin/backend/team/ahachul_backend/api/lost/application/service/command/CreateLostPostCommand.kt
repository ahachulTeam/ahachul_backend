package backend.team.ahachul_backend.api.lost.application.service.command

import backend.team.ahachul_backend.api.lost.domain.model.LostType

class CreateLostPostCommand(
    val title: String,
    val content: String,
    val subwayLine: Long,
    val lostType: LostType,
    val imgUrls: List<String>?
) {
}