package backend.team.ahachul_backend.api.member.adapter.web.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetMemberDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.UpdateMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUseCase
import backend.team.ahachul_backend.api.member.domain.model.GenderType
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
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MemberController::class)
@AutoConfigureRestDocs
class MemberControllerDocsTest(
        @Autowired val mockMvc: MockMvc,
        @Autowired val objectMapper: ObjectMapper
) {

    @MockBean lateinit var memberUseCase: MemberUseCase
    @MockBean lateinit var authenticationInterceptor: AuthenticationInterceptor
    @MockBean lateinit var jpaMetamodelMappingContext: JpaMetamodelMappingContext

    @BeforeEach
    fun setup() {
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true)
    }

    @Test
    fun getMemberTest() {
        // given
        val response = GetMemberDto.Response(
                memberId = 1,
                nickname = "nickname",
                email = "email",
                gender = GenderType.MALE,
                ageRange = "20"
        )

        given(memberUseCase.getMember())
                .willReturn(response)

        // when
        val result = mockMvc.perform(
                get("/v1/members")
                        .header("Authorization", "<Access Token>")
                        .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
                .andDo(document("get-member",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        requestHeaders(
                            headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                fieldWithPath("result.memberId").type(JsonFieldType.NUMBER).description("사용자 Identification Key"),
                                fieldWithPath("result.nickname").type(JsonFieldType.STRING).description("사용자 닉네임").optional(),
                                fieldWithPath("result.email").type(JsonFieldType.STRING).description("사용자 이메일").optional(),
                                fieldWithPath("result.gender").type("GenderType").description("사용자 성별. EX) MALE, FEMALE").optional(),
                                fieldWithPath("result.ageRange").type(JsonFieldType.STRING).description("사용자 연령대").optional(),
                        ))
                )
    }

    @Test
    fun updateMemberTest() {
        // given
        val response = UpdateMemberDto.Response(
                nickname = "nickname",
                gender = GenderType.MALE,
                ageRange = "20"
        )

        given(memberUseCase.updateMember(any()))
                .willReturn(response)

        val request = UpdateMemberDto.Request(
                nickname = "nickname",
                gender = GenderType.MALE,
                ageRange = "20"
        )

        // when
        val result = mockMvc.perform(
                patch("/v1/members")
                        .header("Authorization", "<Access Token>")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
                .andDo(document("update-member",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
                                fieldWithPath("gender").type("GenderType").description("사용자 성별. EX) MALE, FEMALE"),
                                fieldWithPath("ageRange").type(JsonFieldType.STRING).description("사용자 연령대"),

                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                fieldWithPath("result.nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
                                fieldWithPath("result.gender").type("GenderType").description("사용자 성별. EX) MALE, FEMALE"),
                                fieldWithPath("result.ageRange").type(JsonFieldType.STRING).description("사용자 연령대"),
                        ))
                )
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

}