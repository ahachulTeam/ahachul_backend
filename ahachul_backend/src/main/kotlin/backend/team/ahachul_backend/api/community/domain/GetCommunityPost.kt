package backend.team.ahachul_backend.api.community.domain

import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.model.RegionType
import java.time.LocalDateTime

data class GetCommunityPost(
    val id: Long,
    val title: String,
    val content: String,
    val categoryType: CommunityCategoryType,
    val likeCnt: Long,
    val hateCnt: Long,
    val likeYn: Boolean,
    val hateYn: Boolean,
    val regionType: RegionType,
    val subwayLineId: Long,
    val createdAt: LocalDateTime,
    val createdBy: String,
    val writer: String,
) {
}