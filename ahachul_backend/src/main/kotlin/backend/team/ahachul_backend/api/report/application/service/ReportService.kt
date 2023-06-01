package backend.team.ahachul_backend.api.report.application.service

import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostReader
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.report.adpater.`in`.dto.CreateReportDto
import backend.team.ahachul_backend.api.report.application.port.`in`.ReportUseCase
import backend.team.ahachul_backend.api.report.application.port.out.ReportWriter
import backend.team.ahachul_backend.api.report.domain.ReportEntity
import backend.team.ahachul_backend.api.report.domain.ReportType
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReportService(
    private val memberReader: MemberReader,
    private val communityPostReader: CommunityPostReader,
    private val lostPostReader: LostPostReader,
    private val reportWriter: ReportWriter
): ReportUseCase {

    override fun saveReport(targetId: Long, type: String): CreateReportDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val sourceMember = memberReader.getMember(memberId.toLong())

        val target = getTargetAndMemberPair(ReportType.of(type), targetId)
        val targetMember = target.second as MemberEntity
        val entity = reportWriter.saveReport(
            ReportEntity.from(sourceMember, targetMember, target.second)
        )

        return CreateReportDto.Response(
            id = entity.id,
            sourceMemberId = sourceMember.id,
            targetMemberId = targetMember.id,
            targetId = targetId)
    }

    private fun getTargetAndMemberPair(type: ReportType, targetId: Long): Pair<Any, Any> {
        return when (type) {
            ReportType.COMMUNITY -> {
                val reportTarget = communityPostReader.getCommunityPost(targetId)
                reportTarget to reportTarget.member!!
            }

            ReportType.LOST -> {
                val reportTarget = lostPostReader.getLostPost(targetId)
                reportTarget to reportTarget.member
            }
        }
    }
}