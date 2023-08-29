package backend.team.ahachul_backend.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "public-data")
class PublicDataProperties(
    var realTimeCongestionUrl: String = "",
    var realTimeCongestionAppKey: String = ""
)
