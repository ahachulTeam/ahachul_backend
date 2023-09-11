package backend.team.ahachul_backend.api.common.adapter.`in`.dto

import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import java.io.Serializable

class SearchSubwayLineDto {

    data class Response(
        val subwayLines: List<SubwayLine>
    ): Serializable {
        companion object {
            private const val serialVersionUID: Long = -4129628067395047900L
        }
    }
}


data class SubwayLine(
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val stations: List<Station>
): Serializable {

    companion object {
        private const val serialVersionUID: Long = -5129628457305047900L

        fun of(subwayLine: SubwayLineEntity, stations: List<Station>): SubwayLine {
            return SubwayLine(
                id = subwayLine.id,
                name = subwayLine.name,
                phoneNumber = subwayLine.phoneNumber,
                stations = stations
            )
        }
    }
}

data class Station(
    val id: Long,
    val name: String
): Serializable {
    companion object {
        private const val serialVersionUID: Long = -3129628467395847900L
    }
}
