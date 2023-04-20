package backend.team.ahachul_backend.api.lost.adapter.web.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.GetLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.CreateLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.UpdateLostPostDto
import org.springframework.web.bind.annotation.*


@RestController
class LostPostController {

    @GetMapping("/v1/posts/lost/{lostId}")
    fun getLost(@PathVariable("lostId") lostId: Long): GetLostPostDto.Response {
        return GetLostPostDto.Response()
    }

    @GetMapping("/v1/posts/lost")
    fun getLost(@RequestParam("type", required = false) type: String): GetLostPostDto.AllResponse {
        return GetLostPostDto.AllResponse()
    }

    @PostMapping("/v1/posts/lost")
    fun saveLost(@RequestBody request: CreateLostPostDto.Request): CreateLostPostDto.Response {
        return CreateLostPostDto.Response()
    }

    @PatchMapping("/v1/posts/lost/{lostId}")
    fun updateLost(@PathVariable("lostId") lostId: Long,
                   @RequestBody request: UpdateLostPostDto.Request) {

    }

    @DeleteMapping("/v1/posts/lost/{lostId}")
    fun deleteLost(@PathVariable("lostId") lostId: Long) {

    }
}