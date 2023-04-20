package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import org.jetbrains.annotations.NotNull

class CreateLostPostDto{
    data class Request(
        @NotNull var title: String = "",
        @NotNull var content: String = "",
        @NotNull var lostLine: String = "",
        var imgUrls: List<String> = ArrayList()
    )

    data class Response(
        var lostId: Long = 0
    )
}