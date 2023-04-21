package backend.team.ahachul_backend.api.lost.application.port.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.CreateLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.DeleteLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.GetLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.UpdateLostPostDto

interface LostPostUseCase {

    fun getLostPost(): GetLostPostDto.Response

    fun getAllLostPost(): GetLostPostDto.AllResponse

    fun createLostPost(): CreateLostPostDto.Response

    fun updateLostPost(): UpdateLostPostDto.Response

    fun deleteLostPost(): DeleteLostPostDto.Response
}