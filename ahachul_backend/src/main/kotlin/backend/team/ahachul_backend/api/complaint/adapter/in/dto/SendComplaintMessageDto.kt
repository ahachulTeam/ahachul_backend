package backend.team.ahachul_backend.api.complaint.adapter.`in`.dto

import backend.team.ahachul_backend.api.complaint.domain.model.ComplaintType
import backend.team.ahachul_backend.api.complaint.domain.model.ShortContentType

class SendComplaintMessageDto {

    data class Request(
        val content: String,
        val complainType: ComplaintType,
        val shortContentType: ShortContentType? = null,
        val phoneNumber: String,
        val trainNo: String,
        val location: Int,
        val subwayLineId: Long,
    ) {
        fun toCommand(): SendComplaintMessageCommand {
            return SendComplaintMessageCommand(
                content = content,
                complainType = complainType,
                shortContentType = shortContentType,
                phoneNumber = phoneNumber,
                trainNo = trainNo,
                location = location,
                subwayLineId = subwayLineId,
            )
        }
    }
}