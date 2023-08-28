package backend.team.ahachul_backend.api.complaint.application.service

import backend.team.ahachul_backend.api.complaint.adapter.`in`.dto.SendComplaintMessageCommand
import backend.team.ahachul_backend.api.complaint.application.port.`in`.ComplaintUseCase
import backend.team.ahachul_backend.api.complaint.application.port.out.ComplaintWriter
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.persistence.SubwayLineReader
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ComplaintService(
    private val complaintWriter: ComplaintWriter,
    private val memberReader: MemberReader,
    private val subwayLineReader: SubwayLineReader,
): ComplaintUseCase {

    @Transactional
    override fun sendComplaintMessage(command: SendComplaintMessageCommand) {
        val memberId = RequestUtils.getAttribute("memberId")!!
        complaintWriter.save(
            command.toEntity(
                memberReader.getMember(memberId.toLong()),
                subwayLineReader.getById(command.subwayLineId))
        )
    }
}