package backend.team.ahachul_backend.api.report.application.port.`in`

import backend.team.ahachul_backend.api.report.adpater.`in`.dto.ActionReportDto
import backend.team.ahachul_backend.api.report.adpater.`in`.dto.CreateReportDto
import backend.team.ahachul_backend.api.report.application.port.`in`.command.ActionReportCommand

interface ReportUseCase {

    fun saveReport(targetId: Long): CreateReportDto.Response

    fun actionOnReport(command: ActionReportCommand): ActionReportDto.Response
}