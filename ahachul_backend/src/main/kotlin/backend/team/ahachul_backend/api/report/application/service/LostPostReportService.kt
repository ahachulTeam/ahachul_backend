package backend.team.ahachul_backend.api.report.application.service

import backend.team.ahachul_backend.api.lost.application.port.out.LostPostReader
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.api.report.adpater.`in`.dto.CreateReportDto
import backend.team.ahachul_backend.api.report.application.port.`in`.ReportUseCase
import backend.team.ahachul_backend.api.report.application.port.out.ReportWriter
import backend.team.ahachul_backend.api.report.domain.ReportEntity
import backend.team.ahachul_backend.common.exception.DomainException
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LostPostReportService(
    private val lostPostReader: LostPostReader,
    private val memberReader: MemberReader,
    private val reportWriter: ReportWriter
): ReportUseCase {

    override fun saveReport(targetId: Long, type: String): CreateReportDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val sourceMember = memberReader.getMember(memberId.toLong())
        val target = lostPostReader.getLostPost(targetId)
        val targetMember = target.member

        if (sourceMember == targetMember) {
            throw DomainException(ResponseCode.INVALID_REPORT_REQUEST)
        }

        if (target.hasDuplicateReportByMember(sourceMember)) {
            throw DomainException(ResponseCode.DUPLICATE_REPORT_REQUEST)
        }

        val entity = reportWriter.saveReport(
            ReportEntity.from(sourceMember, targetMember, target)
        )

        return CreateReportDto.Response(
            id = entity.id,
            sourceMemberId = sourceMember.id,
            targetMemberId = targetMember.id,
            targetId = targetId
        )
    }
}