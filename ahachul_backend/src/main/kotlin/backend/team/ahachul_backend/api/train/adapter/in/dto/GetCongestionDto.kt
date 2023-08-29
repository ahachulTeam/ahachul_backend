package backend.team.ahachul_backend.api.train.adapter.`in`.dto

import backend.team.ahachul_backend.api.train.domain.CongestionColor

class GetCongestionDto {

    data class Request(
        val subwayLine: Int,
        val trainNo: Int
    )

    data class Response(
        val trainNo: Int,
        val congestions : List<Car>
    )

    data class Car(
        val carNo: Int,
        val congestionColor: String
    ) {
        companion object {
            fun from(carNo: Int, congestionPercent: Int): Car {
                return Car(
                    carNo = carNo,
                    congestionColor = CongestionColor.from(congestionPercent)
                )
            }
        }
    }
}
