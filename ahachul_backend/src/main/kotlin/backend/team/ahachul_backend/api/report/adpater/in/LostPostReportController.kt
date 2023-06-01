package backend.team.ahachul_backend.api.report.adpater.`in`

import backend.team.ahachul_backend.api.report.adpater.`in`.dto.CreateReportDto
import backend.team.ahachul_backend.api.report.application.port.`in`.ReportUseCase
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LostPostReportController(
    private val lostPostReportService: ReportUseCase,
) {
    @PostMapping("/v1/report/lost/{targetId}")
    fun saveReport(@PathVariable("targetId") targetId: Long,
                   @RequestParam("type") type: String): CommonResponse<CreateReportDto.Response> {

        return CommonResponse.success(lostPostReportService.saveReport(targetId, type))
    }
}