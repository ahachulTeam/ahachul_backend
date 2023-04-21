package backend.team.ahachul_backend.api.lost.application.service

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.CreateLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.DeleteLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.GetLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.UpdateLostPostDto
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import org.springframework.stereotype.Service

@Service
class LostPostService: LostPostUseCase {

    override fun getLostPost(): GetLostPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun getAllLostPost(): GetLostPostDto.AllResponse {
        TODO("Not yet implemented")
    }

    override fun createLostPost(): CreateLostPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun updateLostPost(): UpdateLostPostDto.Response {
        TODO("Not yet implemented")
    }

    override fun deleteLostPost(): DeleteLostPostDto.Response {
        TODO("Not yet implemented")
    }
}