package backend.team.ahachul_backend.common.client.impl

import backend.team.ahachul_backend.common.client.KakaoMemberClient
import backend.team.ahachul_backend.common.dto.KakaoAccessTokenDto
import backend.team.ahachul_backend.common.dto.KakaoUserInfoDto
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.response.ResponseCode
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
        private val objectMapper: ObjectMapper
): KakaoMemberClient {

    override fun getAccessTokenByCode(code: String): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val params = LinkedMultiValueMap<String, String>()
        params.add("grant_type", "authorization_code")
        params.add("client_id", "")
        params.add("redirect_uri", "<Redirect_URI>/oauth2/code/kakao")
        params.add("code", code)
        params.add("client_secret", "")

        val request = HttpEntity<MultiValueMap<String, String>>(params, headers)

        val url = "https://kauth.kakao.com/oauth/token"

        val response = restTemplate.postForEntity(url, request, String::class.java)

        try {
            return objectMapper.readValue(response.body, KakaoAccessTokenDto::class.java).accessToken
        } catch (e: JsonProcessingException) {
            throw CommonException(ResponseCode.BAD_REQUEST)
        }
    }

    override fun getUserInfoByAccessToken(accessToken: String): KakaoUserInfoDto {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val params = listOf("kakao_account.name", "kakao_account.email", "kakao_account.age_range", "kakao_account.gender")

        val request = HttpEntity<List<String>>(params, headers)

        val url = "https://kapi.kakao.com/v2/user/me"

        val response = restTemplate.postForEntity(url, request, String::class.java)

        try {
            return objectMapper.readValue(response.body, KakaoUserInfoDto::class.java)
        } catch (e: JsonProcessingException) {
            throw CommonException(ResponseCode.BAD_REQUEST)
        }
    }
}