package backend.team.ahachul_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class AhachulBackendApplication

fun main(args: Array<String>) {
    runApplication<AhachulBackendApplication>(*args)
}
