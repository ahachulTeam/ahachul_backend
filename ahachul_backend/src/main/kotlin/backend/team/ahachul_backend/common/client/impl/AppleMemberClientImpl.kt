package backend.team.ahachul_backend.common.client.impl

import backend.team.ahachul_backend.common.client.AppleMemberClient
import backend.team.ahachul_backend.common.dto.AppleAccessTokenDto
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.properties.JwtProperties
import backend.team.ahachul_backend.common.properties.OAuthProperties
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.JwtUtils
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

class AppleMemberClientImpl(
        private val restTemplate: RestTemplate,
        private val objectMapper: ObjectMapper,
        private val oauthProperties: OAuthProperties,
        private val jwtProperties: JwtProperties,
        private val jwtUtils: JwtUtils
): AppleMemberClient {

    override fun getPublicKey(): String {

    }


    override fun getUserInfoByIdentityToken(accessToken: String): String? {
        return null
    }

    override fun getAccessTokenByCode(identityToken: String, authCode: String): String {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val clientInfo = oauthProperties.client["apple"]
        val providerInfo = oauthProperties.provider["apple"]

        val paramMap = LinkedMultiValueMap<String, String>()
        paramMap.add("client_id", clientInfo!!.clientId)
        paramMap.add("client_secret", generateClientSecret(clientInfo.clientId))
        paramMap.add("code", "CODE")
        paramMap.add("grant_type", authCode)
        paramMap.add("redirect_uri", clientInfo.redirectUri)

        val response = restTemplate.postForObject(providerInfo!!.tokenUri, HttpEntity(paramMap, headers), String::class.java)
        try {
            return objectMapper.readValue(response, AppleAccessTokenDto::class.java).accessToken
        } catch (e: JsonProcessingException) {
            throw CommonException(ResponseCode.BAD_REQUEST)
        }
    }

    override fun getUserInfoByAccessToken(accessToken: String): String? {
        return null
    }

    private fun generateClientSecret(clientId: String): String {
        // https://appleid.apple.com/auth/keys
        return jwtUtils.createToken( "com.test.app",
                "https://appleid.apple.com",
                clientId,
                jwtProperties.accessTokenExpireTime,
                SignatureAlgorithm.ES256)
    }
}