package backend.team.ahachul_backend.api.lost.application.service.command

import backend.team.ahachul_backend.api.lost.domain.entity.CategoryEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

class GetSliceLostPostsCommand(
    val pageable: Pageable,
    val lostType: LostType,
    val lostOrigin: LostOrigin? = null,
    val subwayLine: SubwayLineEntity?,
    val category: CategoryEntity? = null
) {
    companion object {
        fun from(command: SearchLostPostCommand, subwayLine: SubwayLineEntity?): GetSliceLostPostsCommand{
            return GetSliceLostPostsCommand(
                pageable = command.pageable,
                lostType = command.lostType,
                lostOrigin = command.lostOrigin,
                subwayLine = subwayLine,
            )
        }
    }
}
