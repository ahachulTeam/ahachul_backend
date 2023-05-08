package backend.team.ahachul_backend.api.lost.application.service.command

import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import org.springframework.data.domain.Pageable

class SearchLostPostCommand(
    val pageable: Pageable,
    val lostType: LostType,
    val lostOrigin: LostOrigin?,
    val subwayLineId: Long?
){
    companion object {
        fun of(pageable: Pageable, lostType: LostType, line: Long?, origin: LostOrigin?): SearchLostPostCommand {
            return SearchLostPostCommand(
                pageable = pageable,
                lostType = lostType,
                lostOrigin = origin,
                subwayLineId = line,
            )
        }
    }
}