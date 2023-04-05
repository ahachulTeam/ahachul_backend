package backend.team.ahachul_backend.common.client.impl

import backend.team.ahachul_backend.common.client.AppleMemberClient
import backend.team.ahachul_backend.common.dto.ApplePublicKeyDto
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.JwtUtils
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigInteger
import java.security.Key
import java.security.KeyFactory
import java.security.spec.RSAPublicKeySpec
import java.util.*

@Component
class AppleMemberClientImpl(
        private val restTemplate: RestTemplate,
        private val jwtUtils: JwtUtils,
): AppleMemberClient {
    val logger: Logger = Logger(javaClass)
    val objectMapper: ObjectMapper = ObjectMapper()

    override fun verifyIdentityToken(identityToken: String, authCode: String): Boolean {
        if (validateIdentityToken(identityToken)) {
            throw CommonException(ResponseCode.INVALID_APPLE_ID_TOKEN)
        }
        return true
    }

    private fun validateIdentityToken(identityToken: String): Boolean {
        val url = "https://appleid.apple.com/auth/keys"
        val response = restTemplate.getForObject(url, String::class.java)

        val key = objectMapper.readValue(response, ApplePublicKeyDto::class.java)
        val header = getHeaderInfoFromIdentityToken(identityToken)
        val public = key.getMatchedInfo(header?.get("kid") as String, header["alg"] as String)
            ?: throw IllegalArgumentException()

        return try {
            val claim = jwtUtils.verify(identityToken, createPublicKeyByRSA(public)).body
            validateClaimInfo(claim)
        } catch (e: Exception) {
            logger.error(e.message, ResponseCode.BAD_REQUEST, e.stackTraceToString())
            false
        }
    }

    private fun validateClaimInfo(claim: Claims): Boolean {
        val curTime = Date(System.currentTimeMillis())

        return !(curTime.before(claim["exp"] as Date?)
                || claim["iss"] == "https://appleid.apple.com"
                || claim["aud"] == "appleAppId/clientId")
    }

    /**
     * 헤더 부분을 잘라서 토큰 Base64로 복호화
     */
    private fun getHeaderInfoFromIdentityToken(token: String): Map<*, *>? {
        val headerToken = token.substring(0, token.indexOf('.'))
        return objectMapper.readValue(
            String(Base64.getDecoder().decode(headerToken.toByteArray()), Charsets.UTF_8), Map::class.java)
    }

    /**
     * RSA 암호화 방식으로 public key 생성 후 권한 코드 인증
     */
    private fun createPublicKeyByRSA(key: ApplePublicKeyDto.Key): Key {
        val n = BigInteger(1, Base64.getDecoder().decode(key.n))
        val e = BigInteger(1, Base64.getDecoder().decode(key.e))

        val publicKeySpec = RSAPublicKeySpec(n, e)
        val keyFactory = KeyFactory.getInstance(key.kty)
        return keyFactory.generatePublic(publicKeySpec)
    }
}