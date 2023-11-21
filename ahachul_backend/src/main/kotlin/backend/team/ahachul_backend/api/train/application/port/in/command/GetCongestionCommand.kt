package backend.team.ahachul_backend.api.train.application.port.`in`.command

import backend.team.ahachul_backend.api.train.domain.model.UpDownType

class GetCongestionCommand(
    val stationId: Long,
    val subwayLineId: Long,
    val upDownType: UpDownType
)
