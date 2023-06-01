package backend.team.ahachul_backend.api.report.adpater.`in`

import backend.team.ahachul_backend.api.report.adpater.`in`.dto.ActionReportDto
import backend.team.ahachul_backend.api.report.adpater.`in`.dto.CreateReportDto
import backend.team.ahachul_backend.api.report.application.port.`in`.ReportUseCase
import backend.team.ahachul_backend.common.annotation.Authentication
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class CommunityPostReportController(
    private val communityPostReportService: ReportUseCase
) {

    @Authentication
    @PostMapping("/v1/reports/community-post/{targetId}")
    fun saveReport(@PathVariable("targetId") targetId: Long): CommonResponse<CreateReportDto.Response> {
        return CommonResponse.success(communityPostReportService.saveReport(targetId))
    }

    // TODO: 관리자 전용 API
    @PostMapping("/v1/reports/community-post/block")
    fun actionForReport(@RequestBody request: ActionReportDto.Request): CommonResponse<ActionReportDto.Response> {
        return CommonResponse.success(communityPostReportService.actionOnReport(request.toCommand()))
    }
}