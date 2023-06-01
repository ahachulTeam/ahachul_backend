package backend.team.ahachul_backend.api.report.application.service

import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.api.report.adpater.`in`.dto.ActionReportDto
import backend.team.ahachul_backend.api.report.adpater.`in`.dto.CreateReportDto
import backend.team.ahachul_backend.api.report.application.port.`in`.ReportUseCase
import backend.team.ahachul_backend.api.report.application.port.`in`.command.ActionReportCommand
import backend.team.ahachul_backend.api.report.application.port.out.ReportWriter
import backend.team.ahachul_backend.api.report.domain.BlockType
import backend.team.ahachul_backend.api.report.domain.ReportEntity
import backend.team.ahachul_backend.common.client.RedisClient
import backend.team.ahachul_backend.common.exception.DomainException
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit


@Service
@Transactional(readOnly = true)
class CommunityPostReportService(
    private val communityPostReader: CommunityPostReader,
    private val memberReader: MemberReader,
    private val reportWriter: ReportWriter,
    private val redisClient: RedisClient
): ReportUseCase {

    override fun saveReport(targetId: Long): CreateReportDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val sourceMember = memberReader.getMember(memberId.toLong())
        val target = communityPostReader.getCommunityPost(targetId)
        val targetMember = target.member!!

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

    override fun actionOnReport(command: ActionReportCommand): ActionReportDto.Response {
        val blockMemberId = command.targetMemberId
        val member = memberReader.getMember(blockMemberId)
        val blockType = BlockType.of(command.blockType)

        if (!member.isConditionsMetToBlock(blockType.blockDays)) {
            throw DomainException(ResponseCode.INVALID_CONDITION_TO_BLOCK_MEMBER)
        }

        member.blockMember()

        redisClient.set("blocked-member:${blockMemberId}",
            blockMemberId.toString(),
            blockType.blockDays.toLong(),
            TimeUnit.MINUTES
        )
        return ActionReportDto.Response(command.targetMemberId)
    }
}