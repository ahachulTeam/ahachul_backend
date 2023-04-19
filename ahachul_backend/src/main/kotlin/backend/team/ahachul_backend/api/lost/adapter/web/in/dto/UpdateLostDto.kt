package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.model.LostStatus

class UpdateLostDto {
    data class Request(
        var title: String = "",
        var content: String = "",
        var lostLine: String = "",
        var imgUrls: List<String> = ArrayList(),
        var status: LostStatus = LostStatus.PROGRESS
    )
}