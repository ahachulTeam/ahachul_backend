package backend.team.ahachul_backend.api.train.adapter.`in`

import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.config.controller.CommonDocsTestConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(TrainController::class)
class TrainControllerDocsTest : CommonDocsTestConfig() {

    @MockBean
    lateinit var trainUseCase: TrainUseCase

    @Test
    fun getTrainTest() {
        // given
        val response = GetTrainDto.Response(
            id = 1L,
            GetTrainDto.SubwayLine(1L, "1호선"),
            location = 3,
            organizationTrainNo = "52"
        )

        BDDMockito.given(trainUseCase.getTrain(any()))
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/trains/{trainNo}", 1)
                .header("Authorization", "Bearer <Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "get-train",
                    getDocsRequest(),
                    getDocsResponse(),
                    pathParameters(
                        parameterWithName("trainNo").description("열차 번호")
                    ),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    PayloadDocumentation.responseFields(
                        *commonResponseFields(),
                        PayloadDocumentation.fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("열차 식별 번호"),
                        PayloadDocumentation.fieldWithPath("result.subwayLine.id").type(JsonFieldType.NUMBER).description("노선 식별 번호"),
                        PayloadDocumentation.fieldWithPath("result.subwayLine.name").type(JsonFieldType.STRING).description("노선 이름"),
                        PayloadDocumentation.fieldWithPath("result.location").type(JsonFieldType.NUMBER).description("열차 내 현재 위치"),
                        PayloadDocumentation.fieldWithPath("result.organizationTrainNo").type(JsonFieldType.STRING).description("열차 편대 번호"),
                    )
                )
            )
    }
}