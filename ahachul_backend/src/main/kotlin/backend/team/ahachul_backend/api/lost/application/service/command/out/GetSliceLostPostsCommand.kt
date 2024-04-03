package backend.team.ahachul_backend.api.lost.application.service.command.out

import backend.team.ahachul_backend.api.lost.application.service.command.`in`.SearchLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import org.springframework.data.domain.Pageable

class GetSliceLostPostsCommand(
    val lostType: LostType,
    val lostOrigin: LostOrigin?,
    val subwayLine: SubwayLineEntity?,
    val keyword: String?,
    val pageable: Pageable
) {
    companion object {
        fun from(command: SearchLostPostCommand, subwayLine: SubwayLineEntity?): GetSliceLostPostsCommand {
            return GetSliceLostPostsCommand(
                lostType = command.lostType,
                lostOrigin = command.lostOrigin,
                subwayLine = subwayLine,
                keyword = command.keyword,
                pageable = command.pageable
            )
        }
    }
}
