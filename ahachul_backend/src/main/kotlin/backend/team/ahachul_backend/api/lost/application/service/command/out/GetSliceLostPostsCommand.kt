package backend.team.ahachul_backend.api.lost.application.service.command.out

import backend.team.ahachul_backend.api.lost.application.service.command.`in`.SearchLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity

class GetSliceLostPostsCommand(
    val lostType: LostType,
    val lostOrigin: LostOrigin?,
    val subwayLine: SubwayLineEntity?,
    val keyword: String?,
    val lostPostId: Long?,
    val pageSize: Int
) {
    companion object {
        fun from(command: SearchLostPostCommand, subwayLine: SubwayLineEntity?): GetSliceLostPostsCommand {
            return GetSliceLostPostsCommand(
                lostType = command.lostType,
                lostOrigin = command.lostOrigin,
                subwayLine = subwayLine,
                keyword = command.keyword,
                lostPostId = command.lostPostId,
                pageSize = command.pageSize
            )
        }
    }
}
