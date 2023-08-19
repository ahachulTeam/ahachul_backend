package backend.team.ahachul_backend.api.complaint.adapter.`in`

import backend.team.ahachul_backend.api.complaint.adapter.`in`.dto.SendComplaintMessageDto
import backend.team.ahachul_backend.api.complaint.application.port.`in`.ComplaintUseCase
import backend.team.ahachul_backend.common.annotation.Authentication
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ComplaintController(
    private val complaintUseCase: ComplaintUseCase,
) {

    @Authentication
    @PostMapping("/v1/complaints/messages")
    fun sendComplaintMessage(@RequestBody request: SendComplaintMessageDto.Request): CommonResponse<Unit> {
        complaintUseCase.sendComplaintMessage(request.toCommand())
        return CommonResponse.success()
    }
}