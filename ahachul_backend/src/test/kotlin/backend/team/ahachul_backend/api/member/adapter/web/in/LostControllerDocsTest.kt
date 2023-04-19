package backend.team.ahachul_backend.api.member.adapter.web.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.LostController
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.PostLostDto
import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.UpdateLostDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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

@WebMvcTest(LostController::class)
@AutoConfigureRestDocs
class LostControllerDocsTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
){

    @Test
    fun getLost() {
        // when
        val response = mockMvc.perform(
            get("/v1/posts/lost/{lostId}", 1)
                .header("Authorization", "<Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        response.andExpect(status().isOk)
            .andDo(document("get-lost",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("엑세스 토큰")
                ),
                pathParameters(
                    parameterWithName("lostId").description("유실물 아이디")
                ),
                responseFields(
                    fieldWithPath("title").type(JsonFieldType.NUMBER).description("유실물 제목"),
                    fieldWithPath("content").type(JsonFieldType.NUMBER).description("유실물 내용"),
                    fieldWithPath("writer").type(JsonFieldType.NUMBER).description("유실물 작성자 닉네임"),
                    fieldWithPath("date").type(JsonFieldType.NUMBER).description("유실물 작성 날짜"),
                    fieldWithPath("lostLine").type(JsonFieldType.NUMBER).description("유실 호선"),
                    fieldWithPath("chats").type(JsonFieldType.NUMBER).description("유실물 쪽지 개수"),
                    fieldWithPath("imgUrls").type(JsonFieldType.NUMBER).description("유실물 이미지 리스트"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("유실물 찾기 완료 여부 : PROGRESS / COMPLETE"),
                    fieldWithPath("storage" ).type(JsonFieldType.STRING).description("보관 장소 : Lost112 데이터").optional(),
                    fieldWithPath("storage_number").type(JsonFieldType.STRING).description("보관 장소 전화번호 : Lost112 데이터").optional()
                )
            ))
    }

    @Test
    fun getAllLost() {
        // when
        val response = mockMvc.perform(
            get("/v1/posts/lost")
                .header("Authorization", "<Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        response.andExpect(status().isOk)
            .andDo(document("get-all-lost",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("엑세스 토큰")
                ),
                responseFields(
                    fieldWithPath("lostList[].title").type(JsonFieldType.NUMBER).description("유실물 제목"),
                    fieldWithPath("lostList[].content").type(JsonFieldType.NUMBER).description("유실물 내용"),
                    fieldWithPath("lostList[].writer").type(JsonFieldType.NUMBER).description("유실물 작성자 닉네임"),
                    fieldWithPath("lostList[].date").type(JsonFieldType.NUMBER).description("유실물 작성 날짜"),
                    fieldWithPath("lostList[].lostLine").type(JsonFieldType.NUMBER).description("유실 호선"),
                    fieldWithPath("lostList[].chats").type(JsonFieldType.NUMBER).description("유실물 쪽지 개수"),
                    fieldWithPath("lostList[].imgUrl").type(JsonFieldType.NUMBER).description("유실물 이미지(썸네일)"),
                    fieldWithPath("lostList[].status").type(JsonFieldType.STRING).description("유실물 찾기 완료 여부 : PROGRESS / COMPLETE"),
                )
            ))
    }

    @Test
    fun postLost() {
        // give
        val request = PostLostDto.Request(
            title = "title",
            content = "content",
            lostLine = "1",
            imgUrls = arrayListOf("url1", "url2")
        )

        // when
        val response = mockMvc.perform(
            post("/v1/posts/lost")
                .header("Authorization", "<Access Token>")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        response.andExpect(status().isOk)
                .andDo(document("post-lost",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    queryParameters(
                        parameterWithName("type").description("유실물 타입 : ACQUIRE(습득물) / LOST(유실물)")
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("유실물 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("유실물 내용"),
                        fieldWithPath("lostLine").type(JsonFieldType.NUMBER).description("유실 호선"),
                        fieldWithPath("imgUrls").type(JsonFieldType.ARRAY).description("유실물 이미지 리스트"),
                    ),
                    responseFields(
                        fieldWithPath("lostId").type(JsonFieldType.NUMBER).description("저장한 유실물 아이디"),
                    )
                ))
    }

    @Test
    fun updateLost() {
        // give
        val request = UpdateLostDto.Request(
            title = "title",
            content = "content",
            lostLine = "1",
            imgUrls = arrayListOf("url1", "url2")
        )

        // when
        val response = mockMvc.perform(
            patch("/v1/posts/lost/{lostId}")
                .header("Authorization", "<Access Token>")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        response.andExpect(status().isOk)
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
                    fieldWithPath("title").type(JsonFieldType.STRING).description("유실물 제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("유실물 내용"),
                    fieldWithPath("lostLine").type(JsonFieldType.STRING).description("유실 호선 EX) 1호선 / 수인분당선"),
                    fieldWithPath("imgUrls").type(JsonFieldType.ARRAY).description("유실물 이미지 리스트"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("유실물 찾기 완료 상태 : PROGRESS / COMPLETE"),
                )
            ))
    }

    @Test
    fun deleteLost() {
        // when
        val response = mockMvc.perform(
            delete("/v1/posts/lost/{lostId}")
                .header("Authorization", "<Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        response.andExpect(status().isOk)
            .andDo(document("delete-lost",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                pathParameters(
                    parameterWithName("lostId").description("유실물 아이디")
                ),
                requestHeaders(
                    headerWithName("Authorization").description("엑세스 토큰")
                ),
            ))
    }
}