package backend.team.ahachul_backend.common.client

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RedisClientTest(
    @Autowired val redisClient: RedisClient
) {

//    @Test
    @DisplayName("레디스 기본동작 테스트")
    fun 레디스_기본동작_테스트() {
        val test = "TEST"
        redisClient.set(test, test)
        assertThat(redisClient.get(test)).isEqualTo(test)
        redisClient.delete(test)
        assertThat(redisClient.get(test)).isNull()
    }
}