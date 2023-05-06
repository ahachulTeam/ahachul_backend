package backend.team.ahachul_backend.api.lost.application.service.command

import backend.team.ahachul_backend.api.lost.domain.entity.SubwayLine
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import org.springframework.data.domain.Pageable

class GetSliceLostPostsCommand(
    val pageable: Pageable,
    val lostType: LostType,
    val lostOrigin: LostOrigin?,
    val subwayLine: SubwayLine?
) {
    companion object {
        fun from(command: SearchLostPostCommand, subwayLine: SubwayLine?): GetSliceLostPostsCommand{
            return GetSliceLostPostsCommand(
                pageable = command.pageable,
                lostType = command.lostType,
                lostOrigin = command.lostOrigin,
                subwayLine = subwayLine
            )
        }
    }
}