package backend.team.ahachul_backend.common.utils

import backend.team.ahachul_backend.common.properties.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils(
        private val jwtProperties: JwtProperties
) {
    fun createToken(sub: String, sec: Long): String {
        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()))
                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(sec).atZone(ZoneId.of("Asia/Seoul")).toInstant()))
                .signWith(getKey(jwtProperties.secretKey), SignatureAlgorithm.HS256)
                .compact()
    }

    fun createToken(sub: String, iss: String, aud: String, sec: Long, algorithm: SignatureAlgorithm): String {
        return Jwts.builder()
                .setSubject(sub)
                .setIssuer(iss)
                .setAudience(aud)
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()))
                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(sec).atZone(ZoneId.of("Asia/Seoul")).toInstant()))
                .signWith(getKey(jwtProperties.secretKey), algorithm)
                .compact()
    }

    fun verify(token: String): Jws<Claims> {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(jwtProperties.secretKey))
                .build()
                .parseClaimsJws(token)
    }

    private fun getKey(secretKey: String): SecretKey {
        return Keys.hmacShaKeyFor("".toByteArray(StandardCharsets.UTF_8))
    }
}