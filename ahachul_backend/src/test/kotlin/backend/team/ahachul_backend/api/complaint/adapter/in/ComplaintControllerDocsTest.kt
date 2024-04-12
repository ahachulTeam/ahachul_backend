package backend.team.ahachul_backend.api.complaint.adapter.`in`

import backend.team.ahachul_backend.api.complaint.adapter.`in`.dto.SendComplaintMessageDto
import backend.team.ahachul_backend.api.complaint.application.port.`in`.ComplaintUseCase
import backend.team.ahachul_backend.api.complaint.domain.model.ComplaintType
import backend.team.ahachul_backend.api.complaint.domain.model.ShortContentType
import backend.team.ahachul_backend.config.controller.CommonDocsTestConfig
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.willDoNothing
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(ComplaintController::class)
class ComplaintControllerDocsTest: CommonDocsTestConfig() {

    @MockBean
    lateinit var complaintUseCase: ComplaintUseCase

    @Test
    fun sendComplaintMessageTest() {
        // given
        willDoNothing().given(complaintUseCase).sendComplaintMessage(any())

        val request = SendComplaintMessageDto.Request(
            content = "너무 더워요!",
            complainType = ComplaintType.TEMPERATURE_CONTROL,
            shortContentType = ShortContentType.TOO_COLD,
            phoneNumber = "02-234-5678",
            trainNo = "3243",
            location = 2,
            subwayLineId = 1L
        )

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/v1/complaints/messages")
                .header("Authorization", "Bearer <Access Token>")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "send-complaint-message",
                    getDocsRequest(),
                    getDocsResponse(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("content").description("보낸 내용"),
                        PayloadDocumentation.fieldWithPath("complainType").description("민원 타입(ComplaintType)"),
                        PayloadDocumentation.fieldWithPath("shortContentType").description("민원 짧은 내용 타입(ShortContentType)"),
                        PayloadDocumentation.fieldWithPath("phoneNumber").description("보낸 전화번호"),
                        PayloadDocumentation.fieldWithPath("trainNo").description("보낼 때 사용한 열차 번호"),
                        PayloadDocumentation.fieldWithPath("location").description("열차 칸"),
                        PayloadDocumentation.fieldWithPath("subwayLineId").description("보낸 노선 ID"),
                    )
                )
            )
    }
}