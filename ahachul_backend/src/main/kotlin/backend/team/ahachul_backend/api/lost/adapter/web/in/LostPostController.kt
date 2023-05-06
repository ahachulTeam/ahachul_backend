package backend.team.ahachul_backend.api.lost.adapter.web.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.application.service.command.SearchLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.annotation.Authentication
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*


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
        @RequestParam(value = "lostType") lostType: LostType,
        @RequestParam(value = "line", required = false) line: Long,
        @RequestParam(value = "origin", required = false) origin: LostOrigin,
    ): CommonResponse<SearchLostPostsDto.Response> {
        return CommonResponse.success(lostPostService.searchLostPosts(SearchLostPostCommand.of(pageable, lostType, line, origin)))
    }

    @Authentication
    @PostMapping("/v1/lost-posts")
    fun createLostPost(@RequestBody request: CreateLostPostDto.Request): CommonResponse<CreateLostPostDto.Response> {
        return CommonResponse.success(lostPostService.createLostPost(request.toCommand()))
    }

    @Authentication
    @PatchMapping("/v1/lost-posts/{lostId}")
    fun updateLostPost(@PathVariable("lostId") lostId: Long,
                       @RequestBody request: UpdateLostPostDto.Request): CommonResponse<UpdateLostPostDto.Response> {
        return CommonResponse.success(lostPostService.updateLostPost(lostId, request.toCommand()))
    }

    @Authentication
    @DeleteMapping("/v1/lost-posts/{lostId}")
    fun deleteLostPost(@PathVariable("lostId") lostId: Long): CommonResponse<DeleteLostPostDto.Response> {
        return CommonResponse.success(lostPostService.deleteLostPost(lostId))
    }
}