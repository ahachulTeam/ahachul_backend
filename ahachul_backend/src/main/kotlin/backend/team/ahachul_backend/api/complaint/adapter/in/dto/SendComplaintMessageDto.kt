package backend.team.ahachul_backend.api.complaint.adapter.`in`.dto

class SendComplaintMessageDto {

    data class Request(
        val content: String,
        val phoneNumber: String,
        val trainNo: String,
        val subwayLineId: Long,
    ) {
        fun toCommand(): SendComplaintMessageCommand {
            return SendComplaintMessageCommand(
                content = content,
                phoneNumber = phoneNumber,
                trainNo = trainNo,
                subwayLineId = subwayLineId,
            )
        }
    }
}