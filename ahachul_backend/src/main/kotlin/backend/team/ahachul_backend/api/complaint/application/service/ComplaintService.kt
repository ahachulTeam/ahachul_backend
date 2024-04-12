package backend.team.ahachul_backend.api.complaint.application.service

import backend.team.ahachul_backend.api.complaint.adapter.`in`.dto.SearchComplaintMessagesDto
import backend.team.ahachul_backend.api.complaint.adapter.`in`.dto.SendComplaintMessageCommand
import backend.team.ahachul_backend.api.complaint.application.port.`in`.ComplaintUseCase
import backend.team.ahachul_backend.api.complaint.application.port.out.ComplaintReader
import backend.team.ahachul_backend.api.complaint.application.port.out.ComplaintWriter
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.persistence.SubwayLineReader
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ComplaintService(
    private val complaintWriter: ComplaintWriter,
    private val complaintReader: ComplaintReader,
    private val memberReader: MemberReader,
    private val subwayLineReader: SubwayLineReader,
): ComplaintUseCase {

    override fun searchComplaintMessages(pageable: Pageable): SearchComplaintMessagesDto.Response {
        val complaintMessages = complaintReader.findAll(pageable)
            .map { SearchComplaintMessagesDto.ComplaintMessage(
                content = it.sentContent,
                complainType = it.complaintType,
                shortContentType = it.shortContentType,
                complaintMessageStatusType = it.complaintMessageStatusType,
                phoneNumber = it.sentPhoneNumber,
                trainNo = it.sentTrainNo,
                location = it.location,
                subwayLineId = it.subwayLine.id,
                createdAt = it.createdAt,
                createdBy = it.createdBy,
                writer = it.member!!.nickname!!
            ) }

        return SearchComplaintMessagesDto.Response.of(
            hasNext = complaintMessages.size > complaintMessages.pageable.pageSize,
            currentPageNum = pageable.pageNumber,
            complaintMessages = complaintMessages.content
        )
    }

    @Transactional
    override fun sendComplaintMessage(command: SendComplaintMessageCommand) {
        val memberId = RequestUtils.getAttribute("memberId")
        val member = memberId?.let { memberReader.getMember(it.toLong()) }

        complaintWriter.save(
            command.toEntity(
                member,
                subwayLineReader.getById(command.subwayLineId))
        )
    }
}