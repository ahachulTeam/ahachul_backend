package backend.team.ahachul_backend.api.lost.application.port.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.*

interface LostPostUseCase {

    fun getLostPost(): GetLostPostDto.Response

    fun searchLostPosts(): SearchLostPostsDto.Response

    fun createLostPost(): CreateLostPostDto.Response

    fun updateLostPost(): UpdateLostPostDto.Response

    fun deleteLostPost(): DeleteLostPostDto.Response
}