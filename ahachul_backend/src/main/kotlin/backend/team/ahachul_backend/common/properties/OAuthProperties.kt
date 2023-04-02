package backend.team.ahachul_backend.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "oauth")
class OAuthProperties(
        val client: Map<String, Client> = HashMap(),
        val provider: Map<String, Provider> = HashMap()
) {

    data class Client(
            val clientId: String,
            val redirectUri: String
    )

    data class Provider(
            val tokenUri: String,
            val userInfoUri: String
    )
}