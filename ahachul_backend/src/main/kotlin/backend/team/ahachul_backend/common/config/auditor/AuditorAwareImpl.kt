package backend.team.ahachul_backend.common.config.auditor

import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuditorAwareImpl: AuditorAware<String> {

    companion object {
        private const val KEY: String = "memberId"
        private const val UNAUTHORIZED_VALUE: String = "SYSTEM"
    }

    override fun getCurrentAuditor(): Optional<String> {
        val memberId: String = RequestUtils.getAttribute(KEY) ?: UNAUTHORIZED_VALUE
        return Optional.of(memberId)
    }
}