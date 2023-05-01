package backend.team.ahachul_backend.api.member.adapter.web.`in`

import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.CheckNicknameDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetMemberDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.UpdateMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUseCase
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.config.controller.CommonDocsConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MemberController::class)
class MemberControllerDocsTest : CommonDocsConfig() {

    @MockBean
    lateinit var memberUseCase: MemberUseCase

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
                .header("Authorization", "Bearer <Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "get-member",
                    getDocsRequest(),
                    getDocsResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.memberId").type(JsonFieldType.NUMBER).description("사용자 Identification Key"),
                        fieldWithPath("result.nickname").type(JsonFieldType.STRING).description("사용자 닉네임").optional(),
                        fieldWithPath("result.email").type(JsonFieldType.STRING).description("사용자 이메일").optional(),
                        fieldWithPath("result.gender").type("GenderType").description("사용자 성별").attributes(getFormatAttribute("MALE, FEMALE")).optional(),
                        fieldWithPath("result.ageRange").type(JsonFieldType.STRING).description("사용자 연령대").attributes(getFormatAttribute("1 : 1세 이상 10세 미만 ${getNewLine()} 10 : 10세 이상 20세 미만 ${getNewLine()} 20 : 20세 이상 30세 미만 ${getNewLine()} ...")).optional(),
                    )
                )
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
                .header("Authorization", "Bearer <Access Token>")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )


        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "update-member",
                    getDocsRequest(),
                    getDocsResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임").optional(),
                        fieldWithPath("gender").type("GenderType").description("사용자 성별").attributes(getFormatAttribute("MALE, FEMALE")).optional(),
                        fieldWithPath("ageRange").type(JsonFieldType.STRING).description("사용자 연령대").attributes(getFormatAttribute("1 : 1세 이상 10세 미만 ${getNewLine()} 10 : 10세 이상 20세 미만 ${getNewLine()} 20 : 20세 이상 30세 미만 ${getNewLine()} ...")).optional(),

                        ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
                        fieldWithPath("result.gender").type("GenderType").description("사용자 성별").attributes(getFormatAttribute("MALE, FEMALE")),
                        fieldWithPath("result.ageRange").type(JsonFieldType.STRING).description("사용자 연령대").attributes(getFormatAttribute("1 : 1세 이상 10세 미만 ${getNewLine()} 10 : 10세 이상 20세 미만 ${getNewLine()} 20 : 20세 이상 30세 미만 ${getNewLine()} ...")),
                    )
                )
            )
    }

    @Test
    fun checkNicknameTest() {
        // given
        val response = CheckNicknameDto.Response(
            available = true
        )

        given(memberUseCase.checkNickname(any()))
            .willReturn(response)

        val request = CheckNicknameDto.Request(
            nickname = "nickname"
        )

        // when
        val result = mockMvc.perform(
            post("/v1/members/check-nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "check-nickname",
                    getDocsRequest(),
                    getDocsResponse(),
                    requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.available").type(JsonFieldType.BOOLEAN).description("닉네임 사용 가능 여부"),
                    )
                )
            )
    }
}