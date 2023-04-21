package backend.team.ahachul_backend.api.lost.adapter.web.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.CreateLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.DeleteLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.GetLostPostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.UpdateLostPostDto
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.interceptor.AuthenticationInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(LostPostController::class)
@AutoConfigureRestDocs
class LostPostControllerDocsTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
){

    @MockBean lateinit var lostPostUseCase: LostPostUseCase
    @MockBean lateinit var authenticationInterceptor:AuthenticationInterceptor
    @MockBean lateinit var jpaMetamodelMappingContext: JpaMetamodelMappingContext

    @BeforeEach
    fun setup() {
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true)
    }

    @Test
    fun getLostPost() {
        val response = GetLostPostDto.Response(
            title = "title",
            content = "content",
            writer = "writer",
            date = "2023/01/23",
            lostLine = "1호선",
            chats = 1,
            status = LostStatus.PROGRESS,
            imgUrls = listOf(),
            storage = "우리집",
            storageNumber = "02-2222-3333"
        )

        given(lostPostUseCase.getLostPost())
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            get("/v1/posts/lost/{lostId}", 1)
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("get-lost",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                pathParameters(
                    parameterWithName("lostId").description("유실물 아이디")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                    fieldWithPath("result.title").type(JsonFieldType.NUMBER).description("유실물 제목"),
                    fieldWithPath("result.content").type(JsonFieldType.NUMBER).description("유실물 내용"),
                    fieldWithPath("result.writer").type(JsonFieldType.NUMBER).description("유실물 작성자 닉네임"),
                    fieldWithPath("result.date").type(JsonFieldType.NUMBER).description("유실물 작성 날짜"),
                    fieldWithPath("result.lostLine").type(JsonFieldType.NUMBER).description("유실 호선"),
                    fieldWithPath("result.chats").type(JsonFieldType.NUMBER).description("유실물 쪽지 개수"),
                    fieldWithPath("result.imgUrls").type(JsonFieldType.NUMBER).description("유실물 이미지 리스트"),
                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("유실물 찾기 완료 여부 : PROGRESS / COMPLETE"),
                    fieldWithPath("result.storage" ).type(JsonFieldType.STRING).description("보관 장소 : Lost112 데이터"),
                    fieldWithPath("result.storageNumber").type(JsonFieldType.STRING).description("보관 장소 전화번호 : Lost112 데이터")
                )
            ))
    }

    @Test
    fun getAllLostPost() {
        val response = GetLostPostDto.AllResponse(
            listOf(
                GetLostPostDto.Response(
                    title = "title",
                    content = "content",
                    writer = "writer",
                    date = "2023/01/23",
                    lostLine = "1호선",
                    chats = 1,
                    status = LostStatus.PROGRESS,
                    imgUrls = listOf(),
                    storage = "1호선",
                    storageNumber = "02-2222-3333"
                )
            )
        )

        given(lostPostUseCase.getAllLostPost())
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            get("/v1/posts/lost")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("get-all-lost",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                queryParameters(
                    parameterWithName("type").description("유실물 타입 : LOST(유실) / ACQUIRE(습득)"),
                    parameterWithName("line").description("유실물 호선").optional(),
                    parameterWithName("origin").description("유실물 출처 : EXTERNAL(Lost112) / INTERNAL(앱 내부)").optional(),
                    parameterWithName("page").description("현재 페이지"),
                    parameterWithName("size").description("페이지 노출 데이터 수"),
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                    fieldWithPath("result.lostList[].title").type(JsonFieldType.NUMBER).description("유실물 제목"),
                    fieldWithPath("result.lostList[].content").type(JsonFieldType.NUMBER).description("유실물 내용"),
                    fieldWithPath("result.lostList[].writer").type(JsonFieldType.NUMBER).description("유실물 작성자 닉네임"),
                    fieldWithPath("result.lostList[].date").type(JsonFieldType.NUMBER).description("유실물 작성 날짜"),
                    fieldWithPath("result.lostList[].lostLine").type(JsonFieldType.NUMBER).description("유실 호선"),
                    fieldWithPath("result.lostList[].chats").type(JsonFieldType.NUMBER).description("유실물 쪽지 개수"),
                    fieldWithPath("result.lostList[].imgUrl").type(JsonFieldType.NUMBER).description("유실물 이미지(썸네일)"),
                    fieldWithPath("result.lostList[].status").type(JsonFieldType.STRING).description("유실물 찾기 완료 여부 : PROGRESS / COMPLETE"),
                )
            ))
    }

    @Test
    fun createLostPost() {
        val response = CreateLostPostDto.Response(id = 1)

        given(lostPostUseCase.createLostPost())
            .willReturn(response)

        // give
        val request = CreateLostPostDto.Request(
            title = "title",
            content = "content",
            lostLine = "1",
            lostType = LostType.LOST,
            imgUrls = arrayListOf("url1", "url2")
        )

        // when
        val result = mockMvc.perform(
            post("/v1/posts/lost")
                .header("Authorization", "<Access Token>")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
                .andDo(document("post-lost",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("유실물 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("유실물 내용"),
                        fieldWithPath("lostLine").type(JsonFieldType.NUMBER).description("유실 호선 EX) 1호선 / 수인분당선"),
                        fieldWithPath("imgUrls").type(JsonFieldType.ARRAY).description("유실물 이미지 리스트").optional(),
                        fieldWithPath("lostType").type(JsonFieldType.ARRAY).description("유실물 타입 : LOST(유실) / ACQUIRE(습득)"),
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                        fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("저장한 유실물 아이디"),
                    )
                ))
    }

    @Test
    fun updateLostPost() {
        val response = UpdateLostPostDto.Response(id = 1)

        given(lostPostUseCase.updateLostPost())
            .willReturn(response)

        // give
        val request = UpdateLostPostDto.Request(
            title = "title",
            content = "content",
            imgUrls = arrayListOf("url1", "url2"),
            lostLine = "1",
            status = LostStatus.COMPLETE
        )

        // when
        val result = mockMvc.perform(
            patch("/v1/posts/lost/{lostId}", 1)
                .header("Authorization", "<Access Token>")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("update-lost",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("엑세스 토큰")
                ),
                pathParameters(
                    parameterWithName("lostId").description("유실물 아이디")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("유실물 제목").optional(),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("유실물 내용").optional(),
                    fieldWithPath("imgUrls").type(JsonFieldType.ARRAY).description("유실물 이미지 리스트").optional(),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("유실물 찾기 완료 상태 : PROGRESS / COMPLETE").optional(),
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                    fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("수정한 유실물 아이디"),
                )
            ))
    }

    @Test
    fun deleteLostPost() {
        val response = DeleteLostPostDto.Response(id = 1)

        given(lostPostUseCase.deleteLostPost())
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            delete("/v1/posts/lost/{lostId}", 1)
                .header("Authorization", "<Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("delete-lost",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                pathParameters(
                    parameterWithName("lostId").description("유실물 아이디")
                ),
                requestHeaders(
                    headerWithName("Authorization").description("엑세스 토큰")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                    fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("삭제한 유실물 아이디"),
                )
            ))
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}