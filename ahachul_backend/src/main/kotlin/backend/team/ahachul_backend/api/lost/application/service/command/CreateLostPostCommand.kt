package backend.team.ahachul_backend.api.lost.application.service.command

import backend.team.ahachul_backend.api.lost.domain.model.LostCategory

class CreateLostPostCommand(
    val title: String,
    val content: String,
    val subwayLine: Long,
    val lostCategory: LostCategory,
    val imgUrls: List<String>?
) {
}