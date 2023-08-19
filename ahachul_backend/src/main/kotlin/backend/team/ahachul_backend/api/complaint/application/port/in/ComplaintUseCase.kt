package backend.team.ahachul_backend.api.complaint.application.port.`in`

import backend.team.ahachul_backend.api.complaint.adapter.`in`.dto.SendComplaintMessageCommand

interface ComplaintUseCase {

    fun sendComplaintMessage(command: SendComplaintMessageCommand)
}