package backend.team.ahachul_backend.api.train.adapter.`in`.dto

import backend.team.ahachul_backend.api.train.domain.Congestion

class GetCongestionDto {

    data class Request(
        val subwayLine: Int,
        val trainNo: Int
    )

    data class Response(
        val trainNo: Int,
        val congestions : List<Section>
    )

    data class Section(
        val sectionNo: Int,
        val congestionColor: String
    ) {
        companion object {
            fun from(sectionNo: Int, congestionPercent: Int): Section {
                return Section(
                    sectionNo = sectionNo,
                    congestionColor = Congestion.from(congestionPercent)
                )
            }
        }
    }
}
