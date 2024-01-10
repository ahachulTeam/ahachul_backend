package backend.team.ahachul_backend.api.rank.adapter.web.`in`

import backend.team.ahachul_backend.api.rank.application.service.HashTagRankService
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class HashTagRankController(
    private val hashTagRankService: HashTagRankService
) {
    @GetMapping("/v1/ranks/hashtags")
    fun getHashTagRank(): CommonResponse<List<String>> {
        return CommonResponse.success(hashTagRankService.getRank())
    }
}
