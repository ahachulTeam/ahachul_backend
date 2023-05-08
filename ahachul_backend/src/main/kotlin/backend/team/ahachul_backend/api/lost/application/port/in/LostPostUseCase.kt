package backend.team.ahachul_backend.api.lost.application.port.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.SearchLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import org.springframework.data.domain.Pageable

interface LostPostUseCase {

    fun getLostPost(id: Long): GetLostPostDto.Response

    fun searchLostPosts(command: SearchLostPostCommand): SearchLostPostsDto.Response

    fun createLostPost(command: CreateLostPostCommand): CreateLostPostDto.Response

    fun updateLostPost(command: UpdateLostPostCommand): UpdateLostPostDto.Response

    fun deleteLostPost(id: Long): DeleteLostPostDto.Response
}