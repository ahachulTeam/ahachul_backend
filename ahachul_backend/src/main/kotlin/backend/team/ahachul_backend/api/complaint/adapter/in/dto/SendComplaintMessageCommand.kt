package backend.team.ahachul_backend.api.complaint.adapter.`in`.dto

import backend.team.ahachul_backend.api.complaint.domain.entity.ComplaintMessageHistoryEntity
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity

class SendComplaintMessageCommand(
    val content: String,
    val phoneNumber: String,
    val trainNo: String,
    val subwayLineId: Long,
) {

    fun toEntity(member: MemberEntity, subwayLine: SubwayLineEntity): ComplaintMessageHistoryEntity {
        return ComplaintMessageHistoryEntity(
            sentContent = content,
            sentPhoneNumber = phoneNumber,
            sentTrainNo = trainNo,
            member = member,
            subwayLine = subwayLine,
        )
    }
}