package backend.team.ahachul_backend.api.lost.application.service.command

import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType

class SearchLostPostCommand(
    val lostType: LostType,
    val lostOrigin: LostOrigin?,
    val subwayLine: Long?
){
    companion object {
        fun of(origin: LostOrigin, line: Long, lostType: LostType): SearchLostPostCommand {
            return SearchLostPostCommand(
                lostType = lostType,
                lostOrigin = origin,
                subwayLine = line,
            )
        }
    }
}