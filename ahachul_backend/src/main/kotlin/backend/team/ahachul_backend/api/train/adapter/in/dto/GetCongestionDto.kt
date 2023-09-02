package backend.team.ahachul_backend.api.train.adapter.`in`.dto

import backend.team.ahachul_backend.api.train.domain.model.Congestion
import backend.team.ahachul_backend.api.train.domain.model.UpDownType

class GetCongestionDto {

    data class Request(
        val stationId: Long,
        val upDownType: UpDownType
    )

    data class Response(
        val trainNo: Int,
        val congestions : List<Section>
    ) {
        companion object {
            fun from(trainNo: Int, congestions: List<Section>): Response {
                return Response(
                    trainNo = trainNo,
                    congestions = congestions
                )
            }
        }
    }

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
