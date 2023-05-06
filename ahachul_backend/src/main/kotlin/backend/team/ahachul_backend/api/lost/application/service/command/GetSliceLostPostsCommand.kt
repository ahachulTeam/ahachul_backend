package backend.team.ahachul_backend.api.lost.application.service.command

import backend.team.ahachul_backend.api.lost.domain.entity.SubwayLine
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType

class GetSliceLostPostsCommand(
    val lostType: LostType,
    val lostOrigin: LostOrigin?,
    val subwayLine: SubwayLine?
) {
    companion object {
        fun from(command: SearchLostPostCommand, subwayLine: SubwayLine?): GetSliceLostPostsCommand{
            return GetSliceLostPostsCommand(
                lostType = command.lostType,
                lostOrigin = command.lostOrigin,
                subwayLine = subwayLine
            )
        }
    }
}