package backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post

import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import org.springframework.data.domain.Pageable

class SearchCommunityPostCommand(
    val categoryType: CommunityCategoryType? = null,
    val subwayLineId: Long? = null,
    val content: String? = null,
    val pageable: Pageable
) {
}