package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.model.LostStatus

class UpdateLostPostDto {

    data class Request(
        var title: String = "",
        var content: String = "",
        var imgUrls: List<String> = ArrayList(),
        var lostLine: String = "",
        var status: LostStatus = LostStatus.PROGRESS
    )

    data class Response(
        var id: Long = 0,
        var title: String = "",
        var content: String = "",
        var lostLine: String = "",
        var imgUrls: List<String> = ArrayList(),
        var status: LostStatus = LostStatus.PROGRESS
    )
}