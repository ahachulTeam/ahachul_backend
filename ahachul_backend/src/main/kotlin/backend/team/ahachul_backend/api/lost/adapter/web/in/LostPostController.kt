package backend.team.ahachul_backend.api.lost.adapter.web.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.CreateLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.DeleteLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.GetLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.UpdateLostPostDto
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.*


@RestController
class LostPostController(
    private val lostPostService: LostPostUseCase
) {

    @GetMapping("/v1/posts/lost/{lostId}")
    fun getLostPost(@PathVariable("lostId") lostId: Long): CommonResponse<GetLostPostDto.Response> {
        return CommonResponse.success(lostPostService.getLostPost())
    }

    @GetMapping("/v1/posts/lost")
    fun getAllLostPost(@RequestParam("type", required = false) type: LostType): CommonResponse<GetLostPostDto.AllResponse> {
        return CommonResponse.success(lostPostService.getAllLostPost())
    }

    @PostMapping("/v1/posts/lost")
    fun createLostPost(@RequestBody request: CreateLostPostDto.Request): CommonResponse<CreateLostPostDto.Response> {
        return CommonResponse.success(lostPostService.createLostPost())
    }

    @PatchMapping("/v1/posts/lost/{lostId}")
    fun updateLostPost(@PathVariable("lostId") lostId: Long,
                       @RequestBody request: UpdateLostPostDto.Request): CommonResponse<UpdateLostPostDto.Response> {
        return CommonResponse.success(lostPostService.updateLostPost())
    }

    @DeleteMapping("/v1/posts/lost/{lostId}")
    fun deleteLostPost(@PathVariable("lostId") lostId: Long): CommonResponse<DeleteLostPostDto.Response> {
        return CommonResponse.success(lostPostService.deleteLostPost())
    }
}