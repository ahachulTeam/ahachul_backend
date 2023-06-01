package backend.team.ahachul_backend.api.report.domain

import backend.team.ahachul_backend.common.exception.DomainException
import backend.team.ahachul_backend.common.response.ResponseCode
import java.util.*

enum class ReportType {
    COMMUNITY, LOST;

    companion object {
        fun of(type: String): ReportType {
            return values()
                .firstOrNull { x -> x.name == type.uppercase(Locale.getDefault()) }
                ?: throw DomainException(ResponseCode.INVALID_ENUM)
        }
    }
}