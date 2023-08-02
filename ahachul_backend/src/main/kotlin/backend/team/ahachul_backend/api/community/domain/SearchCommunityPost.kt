package backend.team.ahachul_backend.api.community.domain

import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.model.RegionType
import backend.team.ahachul_backend.common.model.YNType
import java.time.LocalDateTime

data class SearchCommunityPost(
    val id: Long,
    val title: String,
    val content: String,
    val categoryType: CommunityCategoryType,
    val regionType: RegionType,
    val subwayLineId: Long,
    val likeCnt: Long,
    val commentCnt: Long,
    val hotPostYn: YNType,
    val createdAt: LocalDateTime,
    val createdBy: String,
    val writer: String,
) {
}