package backend.team.ahachul_backend.api.lost.application.port.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand

interface LostPostUseCase {

    fun getLostPost(): GetLostPostDto.Response

    fun searchLostPosts(): SearchLostPostsDto.Response

    fun createLostPost(command: CreateLostPostCommand): CreateLostPostDto.Response

    fun updateLostPost(): UpdateLostPostDto.Response

    fun deleteLostPost(): DeleteLostPostDto.Response
}