package backend.team.ahachul_backend.common.utils

import org.apache.commons.codec.binary.Base64
import org.slf4j.helpers.MessageFormatter
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object PageTokenUtils {

    private const val PAGE_TOKEN_FORMAT = "{}|{}"

    fun <T, R> encodePageToken(data: Pair<T, R>): String {
        val formatted = MessageFormatter.arrayFormat(PAGE_TOKEN_FORMAT, arrayOf(data.first.toString(), data.second.toString())).message
        return Base64.encodeBase64URLSafeString(formatted.toByteArray(StandardCharsets.UTF_8))
    }

    fun <T, R> decodePageToken(pageToken: String, firstType: Class<T>, secondType: Class<R>): Pair<T, R> {
        val decoded = String(Base64.decodeBase64(pageToken), StandardCharsets.UTF_8)
        val parts = decoded.split("|", limit = 2)
        require(parts.size == 2) { "invalid pageToken" }
        return Pair(stringToValue(parts[0], firstType), stringToValue(parts[1], secondType))
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> stringToValue(data: String, clazz: Class<T>): T {
        return when (clazz) {
            String::class.java -> data as T
            Int::class.java -> data.toInt() as T
            Long::class.java -> data.toLong() as T
            Boolean::class.java -> data.toBoolean() as T
            LocalDateTime::class.java -> LocalDateTime.parse(
                data,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            ) as T
            else -> throw IllegalArgumentException("unsupported type - type: $clazz")
        }
    }
}
