package backend.team.ahachul_backend.api.lost.adapter.web.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.GetLostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.PostLostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.UpdateLostDto
import org.springframework.web.bind.annotation.*


@RestController
class LostController {

    @GetMapping("/v1/posts/lost/{lostId}")
    fun getLost(@PathVariable("lostId") lostId: Long): GetLostDto.Response {
        return GetLostDto.Response()
    }

    @GetMapping("/v1/posts/lost")
    fun getLost(@RequestParam("type", required = false) type: String): GetLostDto.AllResponse {
        return GetLostDto.AllResponse()
    }

    @PostMapping("/v1/posts/lost")
    fun saveLost(@RequestBody request: PostLostDto.Request): PostLostDto.Response {
        return PostLostDto.Response()
    }

    @PatchMapping("/v1/posts/lost/{lostId}")
    fun updateLost(@PathVariable("lostId") lostId: Long,
                   @RequestBody request: UpdateLostDto.Request) {

    }

    @DeleteMapping("/v1/posts/lost/{lostId}")
    fun deleteLost(@PathVariable("lostId") lostId: Long) {

    }
}