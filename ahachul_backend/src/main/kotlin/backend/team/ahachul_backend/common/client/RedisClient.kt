package backend.team.ahachul_backend.common.client

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisClient(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun set(key: String, value: Any) {
        redisTemplate.opsForValue().set(key, value)
    }

    fun set(key: String, value: Any, timeout: Long, timeUnit: TimeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit)
    }

    fun get(key: String): Any? {
        return redisTemplate.opsForValue().get(key)
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }
}