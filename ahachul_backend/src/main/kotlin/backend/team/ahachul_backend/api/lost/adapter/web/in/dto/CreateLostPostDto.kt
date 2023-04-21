package backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto

import backend.team.ahachul_backend.api.lost.domain.model.LostType
import org.jetbrains.annotations.NotNull

class CreateLostPostDto{

    data class Request(
        @NotNull val title: String,
        @NotNull val content: String,
        @NotNull val lostLine: String,
        @NotNull val lostType: LostType,
        val imgUrls: List<String>?
    )

    data class Response(
        val id: Long
    )
}