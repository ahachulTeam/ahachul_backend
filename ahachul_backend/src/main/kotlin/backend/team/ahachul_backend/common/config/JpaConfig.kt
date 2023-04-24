package backend.team.ahachul_backend.common.config

import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class JpaConfig {

    companion object {
        private const val MEMBER_ID = "memberId"
        private const val UNAUTHORIZED_VALUE = "SYSTEM"
    }

    @Bean
    fun auditorProvider() = AuditorAware {
        val memberId: String = RequestUtils.getAttribute(MEMBER_ID) ?: UNAUTHORIZED_VALUE
        Optional.of(memberId)
    }
}