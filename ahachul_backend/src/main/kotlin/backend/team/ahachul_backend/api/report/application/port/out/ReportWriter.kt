package backend.team.ahachul_backend.api.report.application.port.out

import backend.team.ahachul_backend.api.report.domain.ReportEntity

interface ReportWriter {

    fun saveReport(reportEntity: ReportEntity): ReportEntity
}