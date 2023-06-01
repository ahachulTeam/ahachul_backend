package backend.team.ahachul_backend.api.report.adpater.`in`

import backend.team.ahachul_backend.api.report.adpater.`in`.dto.ActionReportDto
import backend.team.ahachul_backend.api.report.adpater.`in`.dto.CreateReportDto
import backend.team.ahachul_backend.api.report.application.port.`in`.ReportUseCase
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.*

@RestController
class LostPostReportController(
    private val lostPostReportService: ReportUseCase,
) {
    @PostMapping("/v1/reports/lost-post/{targetId}")
    fun saveReport(@PathVariable("targetId") targetId: Long): CommonResponse<CreateReportDto.Response> {

        return CommonResponse.success(lostPostReportService.saveReport(targetId))
    }

    // TODO: 관리자 전용 API
    @PostMapping("/v1/reports/lost-post/block")
    fun actionForReport(@RequestBody request: ActionReportDto.Request): CommonResponse<ActionReportDto.Response> {
        return CommonResponse.success(lostPostReportService.actionOnReport(request.toCommand()))
    }
}