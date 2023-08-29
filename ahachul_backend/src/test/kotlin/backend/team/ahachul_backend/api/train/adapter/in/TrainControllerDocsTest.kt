package backend.team.ahachul_backend.api.train.adapter.`in`

import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetCongestionDto
import backend.team.ahachul_backend.api.train.adapter.`in`.dto.GetTrainDto
import backend.team.ahachul_backend.api.train.application.port.`in`.TrainUseCase
import backend.team.ahachul_backend.api.train.application.service.TrainCongestionService
import backend.team.ahachul_backend.api.train.domain.CongestionColor
import backend.team.ahachul_backend.config.controller.CommonDocsTestConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.BDDMockito
import org.mockito.BDDMockito.*
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(TrainController::class)
class TrainControllerDocsTest : CommonDocsTestConfig() {

    @MockBean
    lateinit var trainUseCase: TrainUseCase

    @MockBean
    lateinit var trainCongestionService: TrainCongestionService

    @Test
    fun getTrainTest() {
        // given
        val response = GetTrainDto.Response(
            id = 1L,
            GetTrainDto.SubwayLine(1L, "1호선"),
            location = 3,
            organizationTrainNo = "52"
        )

        given(trainUseCase.getTrain(any()))
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            get("/v1/trains/{trainNo}", 1)
                .header("Authorization", "Bearer <Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "get-train",
                    getDocsRequest(),
                    getDocsResponse(),
                    pathParameters(
                        parameterWithName("trainNo").description("열차 번호")
                    ),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("열차 식별 번호"),
                        fieldWithPath("result.subwayLine.id").type(JsonFieldType.NUMBER).description("노선 식별 번호"),
                        fieldWithPath("result.subwayLine.name").type(JsonFieldType.STRING).description("노선 이름"),
                        fieldWithPath("result.location").type(JsonFieldType.NUMBER).description("열차 내 현재 위치"),
                        fieldWithPath("result.organizationTrainNo").type(JsonFieldType.STRING).description("열차 편대 번호"),
                    )
                )
            )
    }

    @Test
    fun getTrainCongestionTest() {
        // given
        val response = GetCongestionDto.Response(
            trainNo = 2023,
            congestions =
                listOf(
                    GetCongestionDto.Car(
                        carNo = 1,
                        congestionColor = CongestionColor.GREEN.name),
                    GetCongestionDto.Car(
                        carNo = 2,
                        congestionColor = CongestionColor.YELLOW.name)
                )
        )

        given(trainCongestionService.getTrainCongestion(anyInt(), anyInt()))
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            get("/v1/trains/real-times/congestion")
                .header("Authorization", "Bearer <Access Token>")
                .queryParam("subwayLine", "2")
                .queryParam("trainNo", "2023")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "get-train-congestion",
                    getDocsRequest(),
                    getDocsResponse(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    queryParameters(
                        parameterWithName("subwayLine").description("지하철 노선 이름"),
                        parameterWithName("trainNo").description("열차 식별 번호")
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.trainNo").type(JsonFieldType.NUMBER).description("열차 식별 번호"),
                        fieldWithPath("result.congestions[]").type(JsonFieldType.ARRAY).description("열차 혼잡도 배열"),
                        fieldWithPath("result.congestions[].carNo").type(JsonFieldType.NUMBER).description("칸 번호"),
                        fieldWithPath("result.congestions[].congestionColor").type(JsonFieldType.STRING).description("칸 혼잡도 색깔")
                            .attributes(getFormatAttribute("GREEN, YELLOW, ORANGE, RED"))
                    )
                )
            )
    }
}
