package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.model.LostType
import org.jetbrains.annotations.NotNull

class CreateLostPostDto{

    data class Request(
        @NotNull var title: String = "",
        @NotNull var content: String = "",
        @NotNull var lostLine: String = "",
        var imgUrls: List<String> = ArrayList(),
        var lostType: LostType = LostType.LOST
    )

    data class Response(
        var id: Long = 0
    )
}