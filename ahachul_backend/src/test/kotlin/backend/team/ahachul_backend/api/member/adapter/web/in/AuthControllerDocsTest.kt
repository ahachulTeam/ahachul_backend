package backend.team.ahachul_backend.api.member.adapter.web.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetRedirectUrlDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetTokenDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.LoginMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.AuthUseCase
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.common.interceptor.AuthenticationInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AuthController::class)
@AutoConfigureRestDocs
class AuthControllerDocsTest(
        @Autowired val mockMvc: MockMvc,
        @Autowired val objectMapper: ObjectMapper,
) {

    @MockBean lateinit var authUseCase: AuthUseCase
    @MockBean lateinit var authenticationInterceptor: AuthenticationInterceptor
    @MockBean lateinit var jpaMetamodelMappingContext: JpaMetamodelMappingContext

    @BeforeEach
    fun setup() {
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true)
    }

    @Test
    fun getRedirectUrlTest() {
        // given
        val response = GetRedirectUrlDto.Response(
                redirectUrl = "redirectUrl"
        )

        given(authUseCase.getRedirectUrl(any()))
                .willReturn(response)

        // when
        val result = mockMvc.perform(
                get("/v1/auth/redirect-url")
                        .param("providerType", ProviderType.KAKAO.toString())
                        .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
                .andDo(document("get-redirect-url",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("providerType").description("플랫폼 타입. EX) KAKAO, GOOGLE")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                fieldWithPath("result.redirectUrl").type(JsonFieldType.STRING).description("리다이렉트 URL")
                        ))
                )
    }

    @Test
    fun loginTest() {
        // given
        val response = LoginMemberDto.Response(
                memberId = 1.toString(),
                isNeedAdditionalUserInfo = true,
                accessToken = "accessToken",
                accessTokenExpiresIn = 3600,
                refreshToken = "refreshToken",
                refreshTokenExpiresIn = 259200
        )

        given(authUseCase.login(any()))
                .willReturn(response)

        val request = LoginMemberDto.Request(
                providerType = ProviderType.KAKAO,
                providerCode = "providerCode"
        )

        // when
        val result = mockMvc.perform(
                post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
                .andDo(document("login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("providerType").type("ProviderType").description("플랫폼 타입. EX) KAKAO, GOOGLE"),
                                fieldWithPath("providerCode").type(JsonFieldType.STRING).description("인가 코드")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                fieldWithPath("result.memberId").type(JsonFieldType.STRING).description("사용자 Identification Key"),
                                fieldWithPath("result.isNeedAdditionalUserInfo").type(JsonFieldType.BOOLEAN).description("사용자 추가 정보 입력 여부"),
                                fieldWithPath("result.accessToken").type(JsonFieldType.STRING).description("엑세스 토큰. 만료 기간 : 1시간"),
                                fieldWithPath("result.accessTokenExpiresIn").type(JsonFieldType.NUMBER).description("엑세스 토큰 만료 기간"),
                                fieldWithPath("result.refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰. 만료 기간 : 30일"),
                                fieldWithPath("result.refreshTokenExpiresIn").type(JsonFieldType.NUMBER).description("리프레쉬 토큰 만료 기간"),
                        )),
                )
    }

    @Test
    fun getTokenTest() {
        // given
        val response = GetTokenDto.Response(
                accessToken = "accessToken",
                accessTokenExpiresIn = 3600,
                refreshToken = "refreshToken",
                refreshTokenExpiresIn = 259200
        )

        given(authUseCase.getToken(any()))
                .willReturn(response)

        val request = GetTokenDto.Request(
                refreshToken = "refreshToken"
        )

        // when
        val result = mockMvc.perform(
                post("/v1/auth/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
                .andDo(document("get-token",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰"),
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                fieldWithPath("result.accessToken").type(JsonFieldType.STRING).description("엑세스 토큰. 만료 기간 : 1시간"),
                                fieldWithPath("result.accessTokenExpiresIn").type(JsonFieldType.NUMBER).description("엑세스 토큰 만료 기간"),
                                fieldWithPath("result.refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰. 만료 기간 : 30일"),
                                fieldWithPath("result.refreshTokenExpiresIn").type(JsonFieldType.NUMBER).description("리프레쉬 토큰 만료 기간"),
                        )),
                )
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}