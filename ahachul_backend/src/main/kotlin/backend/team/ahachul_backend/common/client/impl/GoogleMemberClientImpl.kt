package backend.team.ahachul_backend.common.client.impl

import backend.team.ahachul_backend.common.client.GoogleMemberClient
import backend.team.ahachul_backend.common.dto.GoogleAccessTokenDto
import backend.team.ahachul_backend.common.dto.GoogleUserInfoDto
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.properties.OAuthProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import kotlin.math.log

@Component
class GoogleMemberClientImpl(
    private val restTemplate: RestTemplate,
    private val oAuthProperties: OAuthProperties
): GoogleMemberClient {
    companion object {
        const val PROVIDER = "google"
    }

    private val client :OAuthProperties.Client =  oAuthProperties.client[PROVIDER]!!
    private val provider :OAuthProperties.Provider =  oAuthProperties.provider[PROVIDER]!!
    val objectMapper: ObjectMapper = ObjectMapper()

    override fun getAccessTokenByCode(code: String): String? {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }
        val httpEntity = HttpEntity(getHttpBodyParams(code), headers)
        val response = restTemplate.exchange(provider.tokenUri, HttpMethod.POST, httpEntity, String::class.java)
        println(response.body)

        if (response.statusCode == HttpStatus.OK) {
            return objectMapper.readValue(response.body, GoogleAccessTokenDto::class.java).accessToken
        }
        return null
    }

    private fun getHttpBodyParams(code: String): LinkedMultiValueMap<String, String?>{
        return LinkedMultiValueMap<String, String?>().apply {
            "client_id" to client.clientId
            "client_secret" to client.clientSecret
            "code" to code
            "grant_type" to "authorization_code"
            "redirect_uri" to client.redirectUri
        }
    }

    override fun getMemberInfoByAccessToken(accessToken: String): GoogleUserInfoDto? {
        val headers = HttpHeaders().apply {
            setBearerAuth(accessToken)
        }
        val httpEntity = HttpEntity<Any>(headers)
        val response = restTemplate.exchange(provider.userInfoUri, HttpMethod.GET, httpEntity, String::class.java)

        if (response.statusCode == HttpStatus.OK) {
            return objectMapper.readValue(response.body, GoogleUserInfoDto::class.java)
         }
        return null
    }
}