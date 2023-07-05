package backend.team.ahachul_backend.api.lost.adapter.web.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.common.annotation.Authentication
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
class LostPostController(
    private val lostPostService: LostPostUseCase
) {

    @GetMapping("/v1/lost-posts/{lostId}")
    fun getLostPost(@PathVariable("lostId") lostId: Long): CommonResponse<GetLostPostDto.Response> {
        return CommonResponse.success(lostPostService.getLostPost(lostId))
    }

    @GetMapping("/v1/lost-posts")
    fun searchLostPosts(
        pageable: Pageable,
        request: SearchLostPostsDto.Request
    ): CommonResponse<SearchLostPostsDto.Response> {
        return CommonResponse.success(lostPostService.searchLostPosts(request.toCommand(pageable)))
    }

    @Authentication
    @PostMapping("/v1/lost-posts")
    fun createLostPost(
        @RequestPart(value = "dto") request: CreateLostPostDto.Request,
        @RequestPart(value = "files", required = false) files: List<MultipartFile>?
    ): CommonResponse<CreateLostPostDto.Response> {
        return CommonResponse.success(lostPostService.createLostPost(request.toCommand(files)))
    }

    @Authentication
    @PostMapping("/v1/lost-posts/{lostId}")
    fun updateLostPost(
        @PathVariable("lostId") lostId: Long,
        @RequestPart(value = "dto") request: UpdateLostPostDto.Request,
        @RequestPart(value = "files", required = false) files: List<MultipartFile>?
    ): CommonResponse<UpdateLostPostDto.Response> {
        return CommonResponse.success(lostPostService.updateLostPost(request.toCommand(lostId, files)))
    }

    @Authentication
    @DeleteMapping("/v1/lost-posts/{lostId}")
    fun deleteLostPost(@PathVariable("lostId") lostId: Long): CommonResponse<DeleteLostPostDto.Response> {
        return CommonResponse.success(lostPostService.deleteLostPost(lostId))
    }
}
