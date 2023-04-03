package backend.team.ahachul_backend.common.client.impl

import backend.team.ahachul_backend.common.client.KakaoMemberClient
import backend.team.ahachul_backend.common.dto.KakaoAccessTokenDto
import backend.team.ahachul_backend.common.dto.KakaoUserInfoDto
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.properties.OAuthProperties
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.WebClientUtils
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class KakaoMemberClientImpl(
        private val restTemplate: RestTemplate,
        private val objectMapper: ObjectMapper,
        private val oAuthProperties: OAuthProperties
): KakaoMemberClient {

    override fun getAccessTokenByCode(code: String): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val params = LinkedMultiValueMap<String, String>()
        params.add("grant_type", "authorization_code")
        params.add("client_id", oAuthProperties.client["kakao"]!!.clientId)
        params.add("redirect_uri", oAuthProperties.client["kakao"]!!.redirectUri)
        params.add("code", code)

        val request = HttpEntity<MultiValueMap<String, String>>(params, headers)

        val url = oAuthProperties.provider["kakao"]!!.tokenUri

        val response = restTemplate.postForEntity(url, request, String::class.java)

        println("Hello WOrld! ${response.body}")

        try {
            return objectMapper.readValue(response.body, KakaoAccessTokenDto::class.java).accessToken
        } catch (e: JsonProcessingException) {
            println(e.stackTrace.contentToString())
            throw CommonException(ResponseCode.BAD_REQUEST)
        }
    }

    override fun getUserInfoByAccessToken(accessToken: String): KakaoUserInfoDto {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val params = LinkedMultiValueMap<String, String>()

        val request = HttpEntity<MultiValueMap<String, String>>(params, headers)

        val url = oAuthProperties.provider["kakao"]!!.userInfoUri

        val response = restTemplate.postForEntity(url, request, String::class.java)

        try {
            return objectMapper.readValue(response.body, KakaoUserInfoDto::class.java)
        } catch (e: JsonProcessingException) {
            throw CommonException(ResponseCode.BAD_REQUEST)
        }
    }
}