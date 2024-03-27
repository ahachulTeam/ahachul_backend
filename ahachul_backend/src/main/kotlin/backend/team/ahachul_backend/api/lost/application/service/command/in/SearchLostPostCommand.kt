package backend.team.ahachul_backend.api.lost.application.service.command.`in`

import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import org.springframework.data.domain.Pageable

class SearchLostPostCommand(
    val lostType: LostType,
    val lostOrigin: LostOrigin?,
    val subwayLineId: Long?,
    val keyword: String?,
    val pageable: Pageable
)
