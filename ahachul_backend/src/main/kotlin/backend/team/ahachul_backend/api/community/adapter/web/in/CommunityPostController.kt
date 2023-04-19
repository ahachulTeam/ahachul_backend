package backend.team.ahachul_backend.api.community.adapter.web.`in`

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityPostUseCase
import backend.team.ahachul_backend.common.annotation.Authentication
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CommunityPostController(
    private val communityPostUseCase: CommunityPostUseCase
) {

    @GetMapping("/v1/community/posts")
    fun searchCommunityPosts(
        pageable: Pageable,
        searchCommunityPostDto: SearchCommunityPostDto.Request
    ): CommonResponse<SearchCommunityPostDto.Response> {
        return CommonResponse.success(communityPostUseCase.searchCommunityPosts())
    }

    @Authentication
    @GetMapping("/v1/community/posts/{postId}")
    fun getCommunityPost(@PathVariable postId: Long): CommonResponse<GetCommunityPostDto.Response> {
        return CommonResponse.success(communityPostUseCase.getCommunityPost())
    }

    @Authentication
    @PostMapping("/v1/community/posts")
    fun createCommunityPost(request: CreateCommunityPostDto.Request): CommonResponse<CreateCommunityPostDto.Response> {
        return CommonResponse.success(communityPostUseCase.createCommunityPost())
    }

    @Authentication
    @PatchMapping("/v1/community/posts/{postId}")
    fun updateCommunityPost(@PathVariable postId: Long, @RequestBody request: UpdateCommunityPostDto.Request): CommonResponse<UpdateCommunityPostDto.Response> {
        return CommonResponse.success(communityPostUseCase.updateCommunityPost())
    }

    @DeleteMapping("/v1/community/posts/{postId}")
    fun deleteCommunityPost(@PathVariable postId: Long): CommonResponse<DeleteCommunityPostDto.Response> {
        return CommonResponse.success(communityPostUseCase.deleteCommunityPost())
    }
}